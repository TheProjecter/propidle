package com.googlecode.propidle.compositeproperties;

import com.googlecode.propidle.PropidlePath;
import com.googlecode.propidle.aliases.AliasesResource;
import com.googlecode.propidle.properties.PropertiesResource;
import com.googlecode.propidle.server.RequestedRevisionNumber;
import com.googlecode.propidle.util.Predicates;
import com.googlecode.propidle.util.collections.MultiMap;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.QueryParameters;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;
import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;
import com.googlecode.utterlyidle.annotations.QueryParam;
import com.googlecode.utterlyidle.rendering.Model;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.propidle.properties.ModelOfProperties.modelOfProperties;
import static com.googlecode.propidle.properties.Properties.compose;
import static com.googlecode.propidle.properties.Properties.key;
import static com.googlecode.propidle.properties.Properties.toPair;
import static com.googlecode.propidle.server.ConvertRevisionNumberQueryParameterToHeader.REVISION_PARAM;
import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.isRight;
import static com.googlecode.totallylazy.Right.right;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Path(CompositePropertiesResource.NAME)
@Produces(TEXT_HTML)
public class CompositePropertiesResource {
    public static final String NAME = "composite";
    private final BasePath basePath;
    private final Option<RequestedRevisionNumber> requestedRevisionNumber;
    private final Application application;
    private final PropidlePath propidlePath;

    public CompositePropertiesResource(BasePath basePath, Option<RequestedRevisionNumber> requestedRevisionNumber, Application application, PropidlePath propidlePath) {
        this.basePath = basePath;
        this.requestedRevisionNumber = requestedRevisionNumber;
        this.application = application;
        this.propidlePath = propidlePath;
    }

    @GET
    public Model getHtml(@QueryParam("alias") Option<String> alias, QueryParameters parameters) {
        Sequence<Uri> urls = sequence(parameters.getValues("url")).filter(Predicates.nonEmpty()).map(com.googlecode.propidle.util.Callables.toUrl()).memorise();

        Sequence<Pair<Uri, Either<Status, Properties>>> urlGetResults = urls.zip(urls.mapConcurrently(toProperties()));

        Sequence<Properties> sourceProperties = urlGetResults.
                map(Callables.<Either<Status, Properties>>second()).
                filter(isRight()).
                map(Callables.right(Properties.class)).
                memorise();

        Sequence<Pair<Uri, Properties>> zip = urls.
                zip(sourceProperties);
        MultiMap<String, Pair<Uri, String>> overrides = zip.
                reverse().
                fold(new MultiMap<String, Pair<Uri, String>>(), collectOverrides());

        Properties compositeProperties = sourceProperties.fold(new Properties(), compose());

        Model propertiesAndOverrides = sequence(compositeProperties.entrySet()).
                sortBy(key()).
                map(toPair()).
                fold(modelWithName(NAME), modelOfPropertiesAndOverrides(overrides)).
                add("revision", requestedRevisionNumber.getOrNull()).
                add("aliasesUrl", model().add("name", propidlePath.path(method(on(AliasesResource.class).listAllAliases()))).add("url", model().add("name", propidlePath.absoluteUriOf(method(on(AliasesResource.class).listAllAliases()))))).
                add("thisUrl", compositeUrlWithAliasRemoved(parameters)).
                add(TITLE, title(urls));

        alias.fold(propertiesAndOverrides, intoModel());

        return urlGetResults.fold(propertiesAndOverrides, urlIntoModel());
    }

    private String compositeUrlWithAliasRemoved(QueryParameters parameters) {
        QueryParameters paramsWithOutAlias = QueryParameters.queryParameters(parameters);
        paramsWithOutAlias.remove("alias");
        return propidlePath.path(method(on(CompositePropertiesResource.class).getHtml(Option.<String>none(), paramsWithOutAlias))).toString();
    }

    private Callable2<Model, String, Model> intoModel() {
        return new Callable2<Model, String, Model>() {
            public Model call(Model model, String value) throws Exception {
                if (value.isEmpty()) {
                    return model;
                }
                return model.add("alias", value);
            }
        };
    }

    private Callable1<Uri, Either<Status, Properties>> toProperties() {
        return new Callable1<Uri, Either<Status, Properties>>() {
            public Either<Status, Properties> call(Uri url) throws Exception {
                RequestBuilder request = RequestBuilder.get(url.toString()).accepting(TEXT_PLAIN);
                if (!requestedRevisionNumber.isEmpty()) {
                    request.withHeader(REVISION_PARAM, requestedRevisionNumber.get().toString());
                }
                Response response = application.handle(request.build());

                if (response.status() == Status.OK) {
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
                map(toProperties()).
                map(Callables.<Properties>right()).
                fold(new Properties(), compose());

        return modelOfProperties(modelWithName(PropertiesResource.PLAIN_NAME), compositeProperties);
    }

    private String title(Sequence<?> urls) {
        return "Composite of: " + urls.toString(" & ");
    }

    private Callable2<Model, Pair<String, String>, Model> modelOfPropertiesAndOverrides(final MultiMap<String, Pair<Uri, String>> overrides) {
        return new Callable2<Model, Pair<String, String>, Model>() {
            public Model call(Model model, Pair<String, String> property) throws Exception {
                Model basicModel = model().
                        add("name", property.first()).
                        add("value", property.second());

                Sequence<Pair<Uri, String>> overridesForThisProperty = sequence(overrides.get(property.first()));

                return model.add("properties", overridesForThisProperty.fold(basicModel, overridesIntoModel()));
            }
        };
    }

    private Callable2<Model, Pair<Uri, String>, Model> overridesIntoModel() {
        return new Callable2<Model, Pair<Uri, String>, Model>() {
            public Model call(Model model, Pair<Uri, String> propertyUrlAndValue) throws Exception {
                return model.
                        add("overrides", model().
                                add("url", propertyUrlAndValue.first()).
                                add("value", propertyUrlAndValue.second()));
            }
        };
    }

    private Callable2<MultiMap<String, Pair<Uri, String>>, Pair<Uri, Properties>, MultiMap<String, Pair<Uri, String>>> collectOverrides() {
        return new Callable2<MultiMap<String, Pair<Uri, String>>, Pair<Uri, Properties>, MultiMap<String, Pair<Uri, String>>>() {
            public MultiMap<String, Pair<Uri, String>> call(MultiMap<String, Pair<Uri, String>> multiMap, Pair<Uri, Properties> urlAndProperties) throws Exception {
                return sequence(urlAndProperties.second().entrySet()).
                        map(toPair()).
                        fold(multiMap, collectOverride(urlAndProperties.first()));
            }
        };
    }


    private Callable2<MultiMap<String, Pair<Uri, String>>, Pair<String, String>, MultiMap<String, Pair<Uri, String>>> collectOverride(final Uri propertyFileUrl) {
        return new Callable2<MultiMap<String, Pair<Uri, String>>, Pair<String, String>, MultiMap<String, Pair<Uri, String>>>() {
            public MultiMap<String, Pair<Uri, String>> call(MultiMap<String, Pair<Uri, String>> map, Pair<String, String> property) throws Exception {
                return map.put(property.first(), pair(propertyFileUrl, property.second()));
            }
        };
    }

    private Callable2<Model, Pair<Uri, Either<Status, Properties>>, Model> urlIntoModel() {
        return new Callable2<Model, Pair<Uri, Either<Status, Properties>>, Model>() {
            public Model call(Model model, Pair<Uri, Either<Status, Properties>> urlAndResult) throws Exception {
                return model.
                        add("urls", model().
                                add("url", urlAndResult.first()).
                                add("status", urlAndResult.second().isRight() ? "ok" : "bad"));
            }
        };
    }

}
