package com.googlecode.propidle.diff;

import com.googlecode.propidle.properties.PropertyComparison;
import com.googlecode.propidle.properties.PropertyDiffTool;
import com.googlecode.propidle.urls.MimeType;
import com.googlecode.propidle.urls.UriGetter;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.rendering.Model;

import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;
import com.googlecode.utterlyidle.annotations.QueryParam;
import com.googlecode.utterlyidle.MediaType;
import java.util.Properties;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.propidle.client.properties.Properties.properties;
import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Right.right;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Path(DiffResource.NAME)
@Produces(MediaType.TEXT_HTML)
public class DiffResource {
    public static final String NAME = "diff";
    private final PropertyDiffTool propertyDiffTool;
    private final UriGetter uriGetter;

    public DiffResource(PropertyDiffTool propertyDiffTool, UriGetter uriGetter) {
        this.propertyDiffTool = propertyDiffTool;
        this.uriGetter = uriGetter;
    }

    @GET
    public Model get() throws Throwable {
         Model urls = modelWithName(NAME).
                add("left", model().
                        add("url", "").
                        add("status", "ok").
                        add("message", "")).
                add("right", model().
                        add("url", "").
                        add("status", "ok" ).
                        add("message", ""));
        return urls;
    }

    @GET
    public Model get(@QueryParam("left") Either<String, UrlWrapper> leftUrl, @QueryParam("right") Either<String, UrlWrapper> rightUrl) {
        Either<String, Properties> leftResult = tryToGetProperties(leftUrl);
        Either<String, Properties> rightResult = tryToGetProperties(rightUrl);

        Sequence<PropertyComparison> diffs = sequence(propertyDiffTool.diffs(safeRight(leftResult), safeRight(rightResult)));
        Model urls = modelWithName(NAME).
                add("left", model().
                        add("url", leftUrl.value()).
                        add("status", leftResult.isRight() ? "ok" : "bad").
                        add("message", safeLeft(leftResult))).
                add("right", model().
                        add("url", rightUrl.value()).
                        add("status", rightResult.isRight() ? "ok" : "bad").
                        add("message", safeLeft(rightResult)));
        return diffs.fold(
                urls,
                addDiffToModel()
        );
    }

    private Either<String, Properties> tryToGetProperties(Either<String, UrlWrapper> url) {
        if (url.isLeft()) return left("Invalid url");
        try {
            Properties properties = propertiesOrEmpty(url.right().url);
            return right(properties);
        } catch (Exception e) {
            return left("Exception occured while getting properties");
        }
    }

    private Callable2<? super Model, ? super PropertyComparison, Model> addDiffToModel() {
        return new Callable2<Model, PropertyComparison, Model>() {
            public Model call(Model model, PropertyComparison comparison) throws Exception {
                return model.add("diffs", model().
                        add("propertyName", comparison.propertyName()).
                        add("previous", comparison.previous()).
                        add("updated", comparison.updated()).
                        add("status", comparison.status().name().toLowerCase())
                );
            }
        };
    }

    private String safeLeft(Either<String, Properties> either) {
        return either.isLeft() ? either.left() : "";
    }


    private Properties safeRight(Either<String, Properties> either) {
        return either.isRight() ? either.right() : new Properties();
    }

    private Properties propertiesOrEmpty(Uri url) throws Exception {
        Properties properties = properties(uriGetter.get(url.toURI(), MimeType.TEXT_PLAIN));
        return properties;
    }

    public static class UrlWrapper {
        public final Uri url;


        public UrlWrapper(String value) {
            this.url = Uri.uri(value);
        }

        @Override
        public String toString() {
            return url.toString();
        }
    }
}
