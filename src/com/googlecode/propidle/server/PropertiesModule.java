package com.googlecode.propidle.server;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.propidle.client.DynamicPropertiesActivator;
import com.googlecode.propidle.client.SnapshotPropertiesActivator;
import com.googlecode.propidle.properties.PropertyDiffTool;
import com.googlecode.propidle.indexing.*;
import com.googlecode.propidle.migrations.MigrationResource;
import com.googlecode.propidle.properties.*;
import com.googlecode.propidle.root.RootResource;
import com.googlecode.propidle.search.*;
import com.googlecode.propidle.server.decoration.DecorateHtml;
import com.googlecode.propidle.server.staticcontent.FavIconResource;
import com.googlecode.propidle.server.staticcontent.StaticContentResource;
import com.googlecode.propidle.urls.RelativeUriGetter;
import com.googlecode.propidle.urls.SimpleUriGetter;
import com.googlecode.propidle.urls.UriGetter;
import com.googlecode.propidle.urls.UrlResolver;
import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.util.time.SystemClock;
import com.googlecode.propidle.versioncontrol.changes.AllChanges;
import com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords;
import com.googlecode.propidle.versioncontrol.changes.ChangesResource;
import com.googlecode.propidle.versioncontrol.revisions.*;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.handlers.ConvertExtensionToAcceptHeader;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.AbstractModule;
import com.googlecode.utterlyidle.modules.ArgumentScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.utterlyidle.sitemesh.Decorators;
import com.googlecode.utterlyidle.sitemesh.SiteMeshHandler;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.Resolver;
import com.googlecode.yadic.generics.TypeFor;
import com.googlecode.yadic.resolvers.OptionResolver;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.lang.reflect.Type;
import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.ModelName.nameIs;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.utterlyidle.handlers.ConvertExtensionToAcceptHeader.Replacements.replacements;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@SuppressWarnings("unchecked")
public class PropertiesModule extends AbstractModule implements ArgumentScopedModule{
    public static final String TITLE = "title";

    public Module addResources(Resources resources) {
        resources.add(PropertiesResource.class);
        return this;
    }

    @Override
    public Module addResponseHandlers(ResponseHandlers handlers) {
        handlers.add(where(entity(Model.class), nameIs(PropertiesResource.HTML_EDITABLE)), renderer(new ModelTemplateRenderer("EditablePropertiesResource_html", PropertiesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(PropertiesResource.HTML_READ_ONLY)), renderer(new ModelTemplateRenderer("PropertiesResource_html", PropertiesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(PropertiesResource.PLAIN_NAME)), renderer(new ModelTemplateRenderer("PropertiesResource_properties", PropertiesResource.class)));
        return this;
    }

    public Module addPerArgumentObjects(final Container container) {
        container.add(PropertiesPath.class, PropertiesPathFromStringResolver.class);
        return this;
    }

    public static class PropertiesPathFromStringResolver implements Resolver<PropertiesPath> {
        private final String theValue;

        public PropertiesPathFromStringResolver(String theValue) {
            this.theValue = theValue;
        }

        public PropertiesPath resolve(Type type) throws Exception {
             return propertiesPath(theValue);
        }
    }
}
