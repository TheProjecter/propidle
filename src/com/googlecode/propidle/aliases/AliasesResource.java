package com.googlecode.propidle.aliases;

import com.googlecode.propidle.ModelName;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.ResourcePath;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.rendering.Model;

import com.googlecode.utterlyidle.annotations.*;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.propidle.aliases.Alias.alias;
import static com.googlecode.propidle.aliases.AliasDestination.aliasDestination;
import static com.googlecode.propidle.aliases.AliasPath.aliasPath;
import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.Responses.seeOther;
import static com.googlecode.utterlyidle.proxy.Resource.redirect;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
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

    public AliasesResource(Aliases aliases, ResourcePath resourcePath, BasePath basePath) {
        this.aliases = aliases;
        this.resourcePath = resourcePath;
        this.basePath = basePath;
    }

    @GET
    public Model listAllAliases() {
        Iterable<Alias> alias = aliases.getAll();

        Model basicModel = modelWithName(ALL_ALIASES).
                add("aliasesUrl", basePath + ALL_ALIASES).
                add(TITLE, "Property file aliases");
        return sequence(alias).fold(basicModel, addAliasToModel());
    }

    @GET
    public Response edit(@QueryParam("from") AliasPath from, @QueryParam("to") AliasDestination overrideDestination) {
        return redirect(resource(AliasesResource.class).edit("", from, some(overrideDestination)));
    }

    @GET
    @Path("{from:.+}")
    public Model edit(@QueryParam("edit") String edit, @PathParam("from") AliasPath from, @QueryParam("to") Option<AliasDestination> overrideDestination) {
        Alias alias = aliases.get(from);
        if (alias == null) {
            alias = alias(aliasPath(""), aliasDestination(""));
        }

        Model model = modelWithName(ALIAS).
                add(TITLE, "Alias \"" + from + "\"").
                add("aliasUrl", basePath + "" + resourcePath).
                add("redirectTo", overrideDestination.isEmpty() ? alias.to() : overrideDestination.get());

        if (!"".equals(alias.to().toString())) {
            model.add("currentRedirectTo", alias.to());
        }

        return model;
    }

    @POST
    @Path("{from:.+}")
    public Response update(@PathParam("from") AliasPath from, @FormParam("to") AliasDestination to) {
        aliases.put(alias(from, to));
        return redirect(resource(AliasesResource.class).edit("", from, none(AliasDestination.class)));
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
            return redirect(resource(AliasesResource.class).edit("", from, none(AliasDestination.class)));
        } else {
            return seeOther(alias.to().url().toString());
        }
    }

    private Callable2<? super Model, ? super Alias, Model> addAliasToModel() {
        return new Callable2<Model, Alias, Model>() {
            public Model call(Model model, Alias alias) throws Exception {
                return model.add("aliases", model().
                        add("from", alias.from()).
                        add("to", alias.to()));
            }
        };
    }
}
