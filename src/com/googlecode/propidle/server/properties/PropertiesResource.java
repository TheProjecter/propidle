package com.googlecode.propidle.server.properties;

import com.googlecode.propidle.AllProperties;
import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.propidle.server.changes.ChangesResource;
import com.googlecode.propidle.server.filenames.FileNamesResource;
import com.googlecode.propidle.versioncontrol.revisions.CurrentRevisionNumber;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.None;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.Redirect;
import com.googlecode.utterlyidle.rendering.Model;

import javax.ws.rs.*;
import java.util.Map;
import java.util.Properties;

import static com.googlecode.propidle.Properties.*;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.proxy.Resource.redirect;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
import static com.googlecode.utterlyidle.proxy.Resource.urlOf;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Path(PropertiesResource.NAME)
@Produces("text/html")
public class PropertiesResource {
    private final AllProperties repository;
    private final BasePath basePath;
    private final CurrentRevisionNumber currentRevisionNumber;
    public static final String NAME = "properties";
    public static final String PLAIN_NAME = NAME + ".properties";
    public static final String HTML_NAME = NAME + ".html";

    public PropertiesResource(AllProperties repository, BasePath basePath, CurrentRevisionNumber currentRevisionNumber) {
        this.repository = repository;
        this.basePath = basePath;
        this.currentRevisionNumber = currentRevisionNumber;
    }

    @GET
    public Redirect getAll() {
        return redirect(resource(FileNamesResource.class).getChildrenOf(propertiesPath("/")));
    }

    @GET
    public Redirect create(@QueryParam("path") PropertiesPath path) {
        return redirect(resource(PropertiesResource.class).getHtml(path, None.<RevisionNumber>none()));
    }

    @GET
    @Path("{path:.+$}")
    @Produces("text/html")
    public Model getHtml(@PathParam("path") PropertiesPath path, @QueryParam("revision")Option<RevisionNumber> revisionNumber) {
        return modelOfProperties(path).
                add(PropertiesModule.MODEL_NAME, HTML_NAME).
                add("changesUrl", basePath + urlOf(resource(ChangesResource.class).get(path, none(RevisionNumber.class))));
    }

    @GET
    @Path("{path:.+$}")
    @Produces("text/plain")
    public Model getProperties(@PathParam("path") PropertiesPath path) {
        return getProperties(path, none(RevisionNumber.class));
    }
    @GET
    @Path("{path:.+$}")
    @Produces("text/plain")
    public Model getProperties(@PathParam("path") PropertiesPath path, @QueryParam("revision")Option<RevisionNumber> revisionNumber) {
        return modelOfProperties(path).add(PropertiesModule.MODEL_NAME, PLAIN_NAME);
    }

    @POST
    @Path("{path:.+$}")
    public Redirect post(@PathParam("path") PropertiesPath path, @FormParam("properties") PropertiesInput propertiesInput) {
        repository.put(path, properties(propertiesInput));
        return redirect(resource(ChangesResource.class).get(path, some(currentRevisionNumber.current())));
    }

    private Model modelOfProperties(PropertiesPath path) {
        Properties properties = repository.get(path);
        return modelOfProperties(properties).
                add("propertiesPath", path).
                add(TITLE, "Properties \"" + path + "\"");
    }

    public static Model modelOfProperties(Properties properties) {
        return sequence(properties.entrySet()).
                sortBy(key()).
                foldLeft(model(), propertyToModel());
    }

    private static Callable2<? super Model, ? super Map.Entry<Object, Object>, Model> propertyToModel() {
        return new Callable2<Model, Map.Entry<Object, Object>, Model>() {
            public Model call(Model model, Map.Entry<Object, Object> entry) throws Exception {
                return model.
                        add("properties",
                            model().
                                    add("name", entry.getKey()).
                                    add("value", entry.getValue()));
            }
        };
    }
}
