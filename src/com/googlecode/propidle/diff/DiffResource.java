package com.googlecode.propidle.diff;

import com.googlecode.propidle.diff.PropertyComparison;
import com.googlecode.propidle.diff.PropertyDiffTool;
import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.propidle.urls.MimeType;
import com.googlecode.propidle.urls.UriGetter;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Left;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.io.Url;
import com.googlecode.utterlyidle.rendering.Model;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Right.right;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.proxy.Resource.redirect;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
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
    public Response get() throws Throwable {
        return redirect(resource(DiffResource.class).get(Left.<String,Url>left(""), Left.<String,Url>left("")));
    }

    @GET
    public Model get(@QueryParam("left") Either<String, Url> leftUrl, @QueryParam("right") Either<String, Url> rightUrl) {
        Either<String, Properties> leftResult = tryToGetProperties(leftUrl);
        Either<String, Properties> rightResult = tryToGetProperties(rightUrl);

        Sequence<PropertyComparison> diffs = sequence(propertyDiffTool.diffs(safeRight(leftResult), safeRight(rightResult)));
        Model urls = model().
                add(PropertiesModule.MODEL_NAME, NAME).
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

    private Either<String, Properties> tryToGetProperties(Either<String, Url> url) {
        if (url.isLeft()) return left("Invalid url");
        try {
            Properties properties = propertiesOrEmpty(url.right());
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

    private Properties propertiesOrEmpty(Url url) throws Exception {
        return properties(uriGetter.get(url.toURI(), MimeType.TEXT_PLAIN));
    }
}
