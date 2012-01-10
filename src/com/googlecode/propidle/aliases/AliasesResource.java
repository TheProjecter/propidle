package com.googlecode.propidle.aliases;

import com.googlecode.propidle.ModelName;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.rendering.Model;

import com.googlecode.utterlyidle.annotations.*;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.propidle.NormalisedHierarchicalPath.removeEndingSlash;
import static com.googlecode.propidle.aliases.Alias.alias;
import static com.googlecode.propidle.aliases.AliasDestination.aliasDestination;
import static com.googlecode.propidle.aliases.AliasPath.aliasPath;
import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.Responses.seeOther;
import static com.googlecode.utterlyidle.rendering.Model.model;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;

@Path(AliasesResource.ALL_ALIASES)
@Produces(TEXT_HTML)
public class AliasesResource {
    public static final String ALL_ALIASES = "aliases";
    public static final String ALIAS = "alias";
    private final Aliases aliases;
    private final ResourcePath resourcePath;
    private final BasePath basePath;
    private final Redirector redirector;

    public AliasesResource(Aliases aliases, ResourcePath resourcePath, BasePath basePath, Redirector redirector) {
        this.aliases = aliases;
        this.resourcePath = resourcePath;
        this.basePath = basePath;
        this.redirector = redirector;
    }

    @GET
    public Model listAllAliases() {
        Iterable<Alias> alias = aliases.getAll();

        Model basicModel = modelWithName(ALL_ALIASES).
                add("aliasesUrl", basePath.subDirectory(ALL_ALIASES).toString()).
                add(TITLE, "Property file aliases");
        return sequence(alias).fold(basicModel, addAliasToModel());
    }

    @GET
    public Response edit(@QueryParam("from") AliasPath from, @QueryParam("to") AliasDestination overrideDestination) {
        return redirector.seeOther(method(on(AliasesResource.class).edit("", from, some(overrideDestination))));
    }

    @GET
    @Path("{from:.+}")
    public Model edit(@QueryParam("edit") String edit, @PathParam("from") AliasPath from, @QueryParam("to") Option<AliasDestination> overrideDestination) {
        Alias alias = aliases.get(from);
        if (alias == null) {
            alias = alias(aliasPath(""), aliasDestination(""));
        }

        AliasDestination redirectTo = overrideDestination.isEmpty() ? alias.to() : overrideDestination.get();

        Model model = modelWithName(ALIAS).
                add(TITLE, "Alias \"" + from + "\"").
                add("aliasUrl", basePath.subDirectory(resourcePath).toString()).
                add("redirectTo", redirectTo);

        if (redirectTo.url().toString().startsWith("/composite?")) {
            model.add("editUrl", redirectTo.url().toString()+"&alias="+UrlEncodedMessage.encode(alias.from().toString()));
        }

        if (!"".equals(alias.to().toString())) {
            model.add("currentRedirectTo", alias.to());
        }

        return model;
    }

    @POST
    @Path("{from:.+}")
    public Response update(@PathParam("from") AliasPath from, @FormParam("to") AliasDestination to) {
        aliases.put(alias(from, to));
        return redirector.seeOther(method(on(AliasesResource.class).edit("", from, none(AliasDestination.class))));
    }

    @GET
    @Path("{from:.+}")
    public Response followRedirectHtml(@PathParam("from") AliasPath from) {
        return redirectFrom(from);
    }

    @GET
    @Path("{from:.+}")
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
                        add("from", removeEndingSlash(basePath.subDirectory(ALL_ALIASES).subDirectory(alias.from()).toString())).
                        add("to", alias.to()));
            }
        };
    }
}
