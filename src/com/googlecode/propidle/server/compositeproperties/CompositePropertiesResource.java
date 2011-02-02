package com.googlecode.propidle.server.compositeproperties;

import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.propidle.server.RequestedRevisionNumber;
import com.googlecode.propidle.server.aliases.AliasesResource;
import com.googlecode.propidle.server.properties.PropertiesResource;
import com.googlecode.propidle.urls.UriGetter;
import com.googlecode.propidle.util.MultiMap;
import com.googlecode.propidle.util.Predicates;
import com.googlecode.totallylazy.*;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.QueryParameters;
import com.googlecode.utterlyidle.io.Url;
import com.googlecode.utterlyidle.rendering.Model;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

import static com.googlecode.propidle.Properties.*;
import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import static com.googlecode.propidle.server.properties.ModelOfProperties.modelOfProperties;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.isLeft;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
import static com.googlecode.utterlyidle.proxy.Resource.urlOf;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Path(CompositePropertiesResource.NAME)
@Produces(TEXT_HTML)
public class CompositePropertiesResource {
    public static final String NAME = "composite";
    private final UriGetter uriGetter;
    private final BasePath basePath;
    private final Option<RequestedRevisionNumber> requestedRevisionNumber;

    public CompositePropertiesResource(UriGetter uriGetter, BasePath basePath, Option<RequestedRevisionNumber> requestedRevisionNumber) {
        this.uriGetter = uriGetter;
        this.basePath = basePath;
        this.requestedRevisionNumber = requestedRevisionNumber;
    }

    @GET
    public Model getHtml(@QueryParam("url") String url, QueryParameters parameters) {
        Sequence<Url> urls = sequence(parameters.getValues("url")).filter(Predicates.nonEmpty()).map(com.googlecode.propidle.util.Callables.toUrl()).memorise();

        Sequence<Pair<Url, Either<String, Exception>>> urlGetResults = urls.
                zip(urls.map(com.googlecode.propidle.util.Callables.eitherExceptionOr(com.googlecode.propidle.util.Callables.urlGet(uriGetter))));

        Sequence<Properties> sourceProperties = urlGetResults.
                map(contentsOrException()).
                filter(isLeft()).
                map(Callables.left(String.class)).
                map(propertiesFromString()).
                memorise();

        Properties compositeProperties = sourceProperties.fold(new Properties(), compose());

        MultiMap<String, Pair<Url, String>> overrides = urls.
                zip(sourceProperties).
                reverse().
                fold(new MultiMap<String, Pair<Url, String>>(), collectOverrides());

        Model propertiesAndOverrides = sequence(compositeProperties.entrySet()).
                sortBy(key()).
                map(toPair()).
                fold(model(), modelOfPropertiesAndOverrides(overrides)).
                add("revision", requestedRevisionNumber.getOrNull()).
                add("aliasesUrl", basePath + AliasesResource.ALL_ALIASES).
                add("thisUrl", basePath + urlOf(resource(CompositePropertiesResource.class).getHtml(url, parameters))).
                add(PropertiesModule.MODEL_NAME, NAME).
                add(TITLE, title(urls));

        return urlGetResults.fold(propertiesAndOverrides, urlIntoModel());
    }

    @GET
    @Produces(TEXT_PLAIN)
    public Model getPlain(@QueryParam("url") String url, QueryParameters parameters) {
        Properties compositeProperties = sequence(parameters.getValues("url")).
                filter(Predicates.nonEmpty()).
                map(com.googlecode.propidle.util.Callables.toUrl()).
                map(com.googlecode.propidle.util.Callables.urlGet(uriGetter)).
                map(propertiesFromString()).
                fold(new Properties(), compose());

        return modelOfProperties(compositeProperties).add(PropertiesModule.MODEL_NAME, PropertiesResource.PLAIN_NAME);
    }

    private String title(Sequence<Url> urls) {
        return "Composite of: " + urls.toString(" & ");
    }

    private Callable2<? super Model, ? super Pair<String, String>, Model> modelOfPropertiesAndOverrides(final MultiMap<String, Pair<Url, String>> overrides) {
        return new Callable2<Model, Pair<String, String>, Model>() {
            public Model call(Model model, Pair<String, String> property) throws Exception {
                Model basicModel = model().
                        add("name", property.first()).
                        add("value", property.second());

                Sequence<Pair<Url, String>> overridesForThisProperty = sequence(overrides.get(property.first()));

                return model.add("properties", overridesForThisProperty.fold(basicModel, overridesIntoModel()));
            }
        };
    }

    private Callable2<? super Model, ? super Pair<Url, String>, Model> overridesIntoModel() {
        return new Callable2<Model, Pair<Url, String>, Model>() {
            public Model call(Model model, Pair<Url, String> propertyUrlAndValue) throws Exception {
                return model.
                        add("overrides", model().
                                add("url", propertyUrlAndValue.first()).
                                add("value", propertyUrlAndValue.second()));
            }
        };
    }

    private Callable2<? super MultiMap<String, Pair<Url, String>>, ? super Pair<Url, Properties>, MultiMap<String, Pair<Url, String>>> collectOverrides() {
        return new Callable2<MultiMap<String, Pair<Url, String>>, Pair<Url, Properties>, MultiMap<String, Pair<Url, String>>>() {
            public MultiMap<String, Pair<Url, String>> call(MultiMap<String, Pair<Url, String>> multiMap, Pair<Url, Properties> urlAndProperties) throws Exception {
                return sequence(urlAndProperties.second().entrySet()).
                        map(toPair()).
                        fold(multiMap, collectOverride(urlAndProperties.first()));
            }
        };
    }

    private Callable2<? super MultiMap<String, Pair<Url, String>>, ? super Pair<String, String>, MultiMap<String, Pair<Url, String>>> collectOverride(final Url propertyFileUrl) {
        return new Callable2<MultiMap<String, Pair<Url, String>>, Pair<String, String>, MultiMap<String, Pair<Url, String>>>() {
            public MultiMap<String, Pair<Url, String>> call(MultiMap<String, Pair<Url, String>> map, Pair<String, String> property) throws Exception {
                return map.put(property.first(), pair(propertyFileUrl, property.second()));
            }
        };
    }

    private Callable2<? super Model, ? super Pair<Url, Either<String, Exception>>, Model> urlIntoModel() {
        return new Callable2<Model, Pair<Url, Either<String, Exception>>, Model>() {
            public Model call(Model model, Pair<Url, Either<String, Exception>> urlAndResult) throws Exception {
                return model.
                        add("urls", model().
                                add("url", urlAndResult.first()).
                                add("status", urlAndResult.second().isLeft() ? "ok" : "bad"));
            }
        };
    }

    private static Callable1<? super Pair<Url, Either<String, Exception>>, Either<String, Exception>> contentsOrException() {
        return second();
    }
}
