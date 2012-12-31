package com.googlecode.propidle.aliases;

import com.googlecode.propidle.PropidlePath;
import com.googlecode.totallylazy.*;
import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.rendering.Model;

import com.googlecode.utterlyidle.annotations.*;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.propidle.NormalisedHierarchicalPath.removeEndingSlash;
import static com.googlecode.propidle.aliases.Alias.alias;
import static com.googlecode.propidle.aliases.Alias.toAliasPath;
import static com.googlecode.propidle.aliases.AliasDestination.aliasDestination;
import static com.googlecode.propidle.aliases.AliasPath.aliasPath;
import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import static com.googlecode.totallylazy.Callables.ascending;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.Responses.seeOther;
import static com.googlecode.utterlyidle.rendering.Model.model;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;

@Produces(TEXT_HTML)
public class AliasesResource {
    public static final String ALL_ALIASES = "aliases";
    public static final String ALIAS = "alias";
    private final Aliases aliases;
    private final ResourcePath resourcePath;
    private final BasePath basePath;
    private final Redirector redirector;
    private final PropidlePath propidlePath;

    public AliasesResource(Aliases aliases, ResourcePath resourcePath, BasePath basePath, Redirector redirector, PropidlePath propidlePath) {
        this.aliases = aliases;
        this.resourcePath = resourcePath;
        this.basePath = basePath;
        this.redirector = redirector;
        this.propidlePath = propidlePath;
    }

    @GET
    @Path(AliasesResource.ALL_ALIASES)
    public Model listAllAliases() {
        return listAliases(none(String.class));
    }

    @GET
    @Path(AliasesResource.ALL_ALIASES)
    public Model listAliases(@QueryParam("filter") String filter) {
        return listAliases(some(filter));
    }

    private Model listAliases(Option<String> filter) {
        Iterable<Alias> alias = sequence(aliases.getAll()).filter(filter.map(toPredicate()).getOrElse(Predicates.always(Alias.class))).sortBy(ascending(toAliasPath()));

        Model basicModel = modelWithName(ALL_ALIASES).
                add("aliasesUrl", aliasesUrlAsModel()).
                add("filter", filter.getOrElse("")).
                add(TITLE, "Property file aliases");
        return sequence(alias).fold(basicModel, addAliasToModel());
    }

    private Callable1<String, Predicate<Alias>> toPredicate() {
        return new Callable1<String, Predicate<Alias>>() {
            @Override
            public Predicate<Alias> call(final String s) throws Exception {
                return new Predicate<Alias>() {
                    @Override
                    public boolean matches(Alias alias) {
                        return alias.from().toString().contains(s); 
                    }
                }; 
            }
        };
    }

    private Model aliasesUrlAsModel() {
        return model().add("name", propidlePath.path(method(on(AliasesResource.class).listAllAliases()))).add("url", basePath.subDirectory(ALL_ALIASES).toString());
    }

    @GET
    @Path(AliasesResource.ALL_ALIASES)
    public Response edit(@QueryParam("from") AliasPath from, @QueryParam("to") AliasDestination overrideDestination) {
        return redirector.seeOther(method(on(AliasesResource.class).edit("", from, some(overrideDestination))));
    }

    @GET
    @Path(AliasesResource.ALL_ALIASES+"{from:.+}")
    public Model edit(@QueryParam("edit") String edit, @PathParam("from") AliasPath from, @QueryParam("to") Option<AliasDestination> overrideDestination) {
        Alias alias = aliases.get(from);
        if (alias == null) {
            alias = alias(aliasPath(""), aliasDestination(""));
        }

        AliasDestination redirectTo = overrideDestination.isEmpty() ? alias.to() : overrideDestination.get();

        Model model = modelWithName(ALIAS).
                add(TITLE, "Alias \"" + from + "\"").
                add("aliasUrl", model().add("name", resourcePath).add("url", basePath.subDirectory(resourcePath).toString())).
                add("redirectTo", redirectTo).
                add("deleteUrl", redirector.absoluteUriOf(method(on(AliasesResource.class).delete(from))));

        if (redirectTo.url().toString().startsWith("/composite?")) {
            model.add("editUrl", redirectTo.url().toString()+"&alias="+UrlEncodedMessage.encode(alias.from().toString()));
        }

        if (!"".equals(alias.to().toString())) {
            model.add("currentRedirectTo", model().add("name", alias.to().toString()).add("url", basePath.file(alias.to().toString())));
        }

        return model;
    }

    @POST
    @Path(AliasesResource.ALL_ALIASES+"{from:.+}")
    public Response update(@PathParam("from") AliasPath from, @FormParam("to") AliasDestination to) {
        aliases.put(alias(from, to));
        return redirector.seeOther(method(on(AliasesResource.class).edit("", from, none(AliasDestination.class))));
    }

    @POST
    @Path(AliasesResource.ALL_ALIASES + "{alias:.+}/delete")
    public Response delete(@PathParam("alias") AliasPath aliasToDelete) {
        aliases.delete(aliasToDelete);
        return redirector.seeOther(method(on(AliasesResource.class).listAllAliases()));
    }

    @GET
    @Path(AliasesResource.ALL_ALIASES+"{from:.+}")
    public Response followRedirectHtml(@PathParam("from") AliasPath from) {
        return redirectFrom(from);
    }

    @GET
    @Path(AliasesResource.ALL_ALIASES+"{from:.+}")
    @Produces(TEXT_PLAIN)
    public Response followRedirectPlain(@PathParam("from") AliasPath from) {
        return redirectFrom(from);
    }

    private Response redirectFrom(AliasPath from) {
        Alias alias = aliases.get(from);
        if (alias == null) {
            return redirector.seeOther(method(on(AliasesResource.class).edit("", from, none(AliasDestination.class))));
        } else {
            return seeOther(alias.to().url().toString());
        }
    }

    private Callable2<? super Model, ? super Alias, Model> addAliasToModel() {
        return new Callable2<Model, Alias, Model>() {
            public Model call(Model model, Alias alias) throws Exception {
                return model.add("aliases", model().
                        add("from", model().add("name", alias.from().toString()).add("url", removeEndingSlash(basePath.subDirectory(ALL_ALIASES).subDirectory(alias.from()).toString()))).
                        add("to", model().add("name", alias.to().toString()).add("url", basePath.file(alias.to().toString()))));
            }
        };
    }
}
