package com.googlecode.propidle.compositeproperties;

import com.googlecode.propidle.aliases.AliasesResource;
import com.googlecode.propidle.properties.PropertiesResource;
import com.googlecode.propidle.server.RequestedRevisionNumber;
import com.googlecode.propidle.urls.MimeType;
import com.googlecode.propidle.urls.UriGetter;
import com.googlecode.propidle.util.Predicates;
import com.googlecode.propidle.util.collections.MultiMap;
import com.googlecode.totallylazy.*;
import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.io.Url;
import com.googlecode.utterlyidle.rendering.Model;

import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;
import com.googlecode.utterlyidle.annotations.QueryParam;
import com.googlecode.utterlyidle.MediaType;
import java.io.ByteArrayInputStream;
import java.util.Properties;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.propidle.properties.ModelOfProperties.modelOfProperties;
import static com.googlecode.propidle.properties.Properties.*;
import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.isLeft;
import static com.googlecode.totallylazy.Predicates.isRight;
import static com.googlecode.totallylazy.Right.right;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
import static com.googlecode.utterlyidle.proxy.Resource.urlOf;
import static com.googlecode.utterlyidle.rendering.Model.model;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;

@Path(CompositePropertiesResource.NAME)
@Produces(TEXT_HTML)
public class CompositePropertiesResource {
    public static final String NAME = "composite";
    private final UriGetter uriGetter;
    private final BasePath basePath;
    private final Option<RequestedRevisionNumber> requestedRevisionNumber;
    private final Application application;

    public CompositePropertiesResource(UriGetter uriGetter, BasePath basePath, Option<RequestedRevisionNumber> requestedRevisionNumber, Application application) {
        this.uriGetter = uriGetter;
        this.basePath = basePath;
        this.requestedRevisionNumber = requestedRevisionNumber;
        this.application = application;
    }

    @GET
    public Model getHtml(@QueryParam("url") String url, QueryParameters parameters) {
        Sequence<Url> urls = sequence(parameters.getValues("url")).filter(Predicates.nonEmpty()).map(com.googlecode.propidle.util.Callables.toUrl()).memorise();

        Sequence<Pair<Url, Either<Status, Properties>>> urlGetResults = urls.zip(urls.mapConcurrently(toProperties()));

        Sequence<Properties> sourceProperties = urlGetResults.
                map(Callables.<Either<Status, Properties>>second()).
                filter(isRight()).
                map(Callables.right(Properties.class)).
                memorise();

        Sequence<Pair<Url, Properties>> zip = urls.
                zip(sourceProperties);
        MultiMap<String, Pair<Url, String>> overrides = zip.
               reverse().
               fold(new MultiMap<String, Pair<Url, String>>(), collectOverrides());

       Properties compositeProperties = sourceProperties.fold(new Properties(), compose());

       Model propertiesAndOverrides = sequence(compositeProperties.entrySet()).
               sortBy(key()).
               map(toPair()).
               fold(modelWithName(NAME), modelOfPropertiesAndOverrides(overrides)).
               add("revision", requestedRevisionNumber.getOrNull()).
               add("aliasesUrl", basePath + AliasesResource.ALL_ALIASES).
               add("thisUrl", basePath + urlOf(resource(CompositePropertiesResource.class).getHtml(url, parameters))).
               add(TITLE, title(urls));

       return urlGetResults.fold(propertiesAndOverrides, urlIntoModel());
    }

    private Callable1<Url, Either<Status, Properties>> toProperties() {
        return new Callable1<Url, Either<Status, Properties>>() {
            public Either<Status, Properties> call(Url url) throws Exception {
                Response response = application.handle(RequestBuilder.get(url.toString()).accepting(TEXT_PLAIN).build());
                if( response.status() == Status.OK) {
                    Properties properties = new Properties();
                    properties.load(new ByteArrayInputStream(response.bytes()));
                    return right(properties);
                }
                return left(response.status());
            }
        };
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

        return modelOfProperties(modelWithName(PropertiesResource.PLAIN_NAME), compositeProperties);
    }

    private String title(Sequence<?> urls) {
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

    private Callable2<? super Model, ? super Pair<Url, Either<Status, Properties>>, Model> urlIntoModel() {
        return new Callable2<Model, Pair<Url, Either<Status, Properties>>, Model>() {
            public Model call(Model model, Pair<Url, Either<Status, Properties>> urlAndResult) throws Exception {
                return model.
                        add("urls", model().
                                add("url", urlAndResult.first()).
                                add("status", urlAndResult.second().isRight() ? "ok" : "bad"));
            }
        };
    }

}
