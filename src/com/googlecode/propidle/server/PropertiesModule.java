package com.googlecode.propidle.server;

import com.googlecode.propidle.properties.AllPropertiesFromChanges;
import com.googlecode.propidle.diff.PropertyDiffTool;
import com.googlecode.propidle.aliases.Aliases;
import com.googlecode.propidle.aliases.AliasesFromRecords;
import com.googlecode.propidle.aliases.AliasesResource;
import com.googlecode.propidle.authorisation.users.*;
import com.googlecode.propidle.indexing.*;
import com.googlecode.propidle.search.FileNameSearcher;
import com.googlecode.propidle.search.LuceneFileNameSearcher;
import com.googlecode.propidle.search.LucenePropertiesSearcher;
import com.googlecode.propidle.search.PropertiesSearcher;
import com.googlecode.propidle.versioncontrol.changes.ChangesResource;
import com.googlecode.propidle.compositeproperties.CompositePropertiesResource;
import com.googlecode.propidle.server.decoration.DecorateHtml;
import com.googlecode.propidle.diff.DiffResource;
import com.googlecode.propidle.filenames.FileNamesResource;
import com.googlecode.propidle.properties.AllProperties;
import com.googlecode.propidle.properties.*;
import com.googlecode.propidle.root.RootResource;
import com.googlecode.propidle.search.LuceneIndexWriterTransaction;
import com.googlecode.propidle.search.SearchResource;
import com.googlecode.propidle.authentication.SessionStarter;
import com.googlecode.propidle.authentication.Sessions;
import com.googlecode.propidle.authentication.*;
import com.googlecode.propidle.authentication.SessionsFromRecords;
import com.googlecode.propidle.server.staticcontent.FavIconResource;
import com.googlecode.propidle.server.staticcontent.StaticContentResource;
import com.googlecode.propidle.authorisation.UsersResource;
import com.googlecode.propidle.authorisation.GroupsResource;
import com.googlecode.propidle.urls.RelativeUriGetter;
import com.googlecode.propidle.urls.SimpleUriGetter;
import com.googlecode.propidle.urls.UriGetter;
import com.googlecode.propidle.urls.UrlResolver;
import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.util.time.SystemClock;
import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.propidle.versioncontrol.changes.AllChanges;
import com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords;
import com.googlecode.propidle.versioncontrol.revisions.*;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.handlers.ConvertExtensionToAcceptHeader;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.AbstractModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.utterlyidle.sitemesh.Decorators;
import com.googlecode.utterlyidle.sitemesh.SiteMeshHandler;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.resolvers.OptionResolver;
import com.googlecode.yadic.generics.TypeFor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import static com.googlecode.propidle.authorisation.users.PasswordSalt.passwordSalt;
import com.googlecode.propidle.authorisation.groups.GroupsFromRecords;
import com.googlecode.propidle.authorisation.groups.Groups;
import com.googlecode.propidle.authorisation.groups.GroupMemberships;
import com.googlecode.propidle.authorisation.groups.GroupMembershipsFromRecords;
import com.googlecode.propidle.authorisation.permissions.GroupPermissionsFromRecords;
import com.googlecode.propidle.authorisation.permissions.GroupPermissions;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.utterlyidle.handlers.ConvertExtensionToAcceptHeader.Replacements.replacements;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;

import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@SuppressWarnings("unchecked")
public class PropertiesModule extends AbstractModule {
    public static final String MODEL_NAME = "MODEL_NAME";
    private final Directory directory;
    public static final String TITLE = "title";


    public PropertiesModule(Directory directory) {
        if (directory == null) throw new NullArgumentException("directory");
        this.directory = directory;
    }

    public Module addPerApplicationObjects(Container container) {
        container.addInstance(Version.class, Version.LUCENE_30);
        container.addInstance(Directory.class, directory);
        container.addInstance(Analyzer.class, new StandardAnalyzer(Version.LUCENE_30));
        container.addActivator(IndexWriter.class, IndexWriterActivator.class);
        return this;
    }

    public Module addPerRequestObjects(Container container) {
        container.addInstance(ConvertExtensionToAcceptHeader.Replacements.class,
                              replacements(pair("properties", TEXT_PLAIN), pair("html", TEXT_HTML)));
        container.decorate(HttpHandler.class, ConvertExtensionToAcceptHeader.class);
        container.decorate(HttpHandler.class, ConvertRevisionNumberQueryParameterToHeader.class);
        container.decorate(HttpHandler.class, TransactionDecorator.class);
        container.decorate(HttpHandler.class, LuceneIndexWriterTransaction.class);
        container.decorate(HttpHandler.class, SiteMeshHandler.class);

        container.add(Decorators.class, DecorateHtml.class);

        container.add(Clock.class, SystemClock.class);
        container.add(PropertyDiffTool.class, PropertyDiffTool.class);
        container.add(UrlResolver.class, UtterlyIdleUrlResolver.class);
        container.addInstance(PasswordSalt.class, passwordSalt("maldon"));
        container.add(PasswordHasher.class, Sha1PasswordHasher.class);
        container.add(SessionStarter.class);
        container.add(Authenticator.class, AuthenticateAgainstUsers.class);
        container.addActivator(HighestExistingRevisionNumber.class, HighestExistingRevisionNumberActivator.class);
        container.addActivator(RequestedRevisionNumber.class, RequestedRevisionNumberActivator.class);
        container.add(new TypeFor<Option<RequestedRevisionNumber>>(){{}}.get(), new OptionResolver(container, instanceOf(RequestedRevisionNumberActivator.class)));

        container.add(PropertiesIndexer.class, LucenePropertiesIndexer.class);
        container.add(PropertiesSearcher.class, LucenePropertiesSearcher.class);
        container.add(FileNameIndexer.class, LuceneFileNameIndexer.class);
        container.add(FileNameSearcher.class, LuceneFileNameSearcher.class);

        container.add(AllProperties.class, AllPropertiesFromChanges.class);
        container.decorate(AllProperties.class, PropertiesIndexingDecorator.class);
        container.decorate(AllProperties.class, FileNameIndexingDecorator.class);

        container.add(Aliases.class, AliasesFromRecords.class);
        container.add(HighestRevisionNumbers.class, HighestRevisionNumbersFromRecords.class);
        container.decorate(HighestRevisionNumbers.class, LockHighestRevisionNumbersDecorator.class);
        container.add(AllChanges.class, AllChangesFromRecords.class);
        container.add(Users.class, UsersFromRecords.class);
        container.add(Groups.class, GroupsFromRecords.class);
        container.add(GroupPermissions.class, GroupPermissionsFromRecords.class);
        container.add(GroupMemberships.class, GroupMembershipsFromRecords.class);
        container.add(Sessions.class, SessionsFromRecords.class);

        container.add(UriGetter.class, SimpleUriGetter.class);
        container.decorate(UriGetter.class, RelativeUriGetter.class);

        return this;
    }

    public Module addResources(Resources resources) {
        resources.add(AuthenticationResource.class);
        resources.add(RootResource.class);
        resources.add(PropertiesResource.class);
        resources.add(FileNamesResource.class);
        resources.add(CompositePropertiesResource.class);
        resources.add(AliasesResource.class);
        resources.add(ChangesResource.class);
        resources.add(DiffResource.class);
        resources.add(SearchResource.class);
        resources.add(StaticContentResource.class);
        resources.add(FavIconResource.class);
        resources.add(UsersResource.class);
        resources.add(GroupsResource.class);
        return this;
    }


    @Override
    public Module addResponseHandlers(ResponseHandlers handlers) {
        handlers.add(where(entity(Model.class), nameIs(AuthenticationResource.NAME)), renderer(new ModelTemplateRenderer("AuthenticationResource_html", AuthenticationResource.class)));
        handlers.add(where(entity(Model.class), nameIs(PropertiesResource.HTML_EDITABLE)), renderer(new ModelTemplateRenderer("EditablePropertiesResource_html", PropertiesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(PropertiesResource.HTML_READ_ONLY)), renderer(new ModelTemplateRenderer("PropertiesResource_html", PropertiesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(PropertiesResource.PLAIN_NAME)), renderer(new ModelTemplateRenderer("PropertiesResource_properties", PropertiesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(FileNamesResource.NAME)), renderer(new ModelTemplateRenderer("FileNamesResource_search_html", FileNamesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(FileNamesResource.DIRECTORY_VIEW_NAME)), renderer(new ModelTemplateRenderer("FileNamesResource_directories_html", FileNamesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(AliasesResource.ALIAS)), renderer(new ModelTemplateRenderer("AliasResource_html", AliasesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(AliasesResource.ALL_ALIASES)), renderer(new ModelTemplateRenderer("AliasesResource_html", AliasesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(ChangesResource.NAME)), renderer(new ModelTemplateRenderer("ChangesResource_html", ChangesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(DiffResource.NAME)), renderer(new ModelTemplateRenderer("DiffResource_html", DiffResource.class)));
        handlers.add(where(entity(Model.class), nameIs(SearchResource.NAME)), renderer(new ModelTemplateRenderer("SearchResource_html", SearchResource.class)));
        handlers.add(where(entity(Model.class), nameIs(CompositePropertiesResource.NAME)), renderer(new ModelTemplateRenderer("CompositePropertiesResource_html", CompositePropertiesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(UsersResource.NAME)), renderer(new ModelTemplateRenderer("User_html", UsersResource.class)));
        return this;
    }

    private LogicalPredicate<Model> nameIs(final String name) {
        Predicate<Model> nameMatcher = new Predicate<Model>() {
            public boolean matches(Model model) {
                return model != null && model.containsKey(PropertiesModule.MODEL_NAME) && name.equals(model.first(PropertiesModule.MODEL_NAME));
            }
        };
        return and(notNull(Model.class), nameMatcher);
    }

}
