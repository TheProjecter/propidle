package com.googlecode.propidle.server.properties;

import com.googlecode.propidle.AllProperties;
import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.propidle.server.changes.ChangesResource;
import com.googlecode.propidle.server.filenames.FileNamesResource;
import com.googlecode.propidle.versioncontrol.revisions.CurrentRevisionNumber;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.None;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.Redirect;
import com.googlecode.utterlyidle.rendering.Model;

import javax.ws.rs.*;

import static com.googlecode.propidle.Properties.properties;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.utterlyidle.proxy.Resource.*;

@Path(PropertiesResource.NAME)
@Produces("text/html")
public class PropertiesResource {
    private final AllProperties repository;
    private final BasePath basePath;
    private final CurrentRevisionNumber currentRevisionNumber;
    public static final String NAME = "properties";
    public static final String PLAIN_NAME = NAME + ".properties";
    public static final String HTML_EDITABLE = NAME + ".html";
    public static final String HTML_READ_ONLY = NAME + ".readonly.html";

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
    public Model getHtml(@PathParam("path") PropertiesPath path, @QueryParam("revision") Option<RevisionNumber> revisionNumber) {
        return modelOf(path, revisionNumber).
                add(PropertiesModule.MODEL_NAME, modelName(revisionNumber)).
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
    public Model getProperties(@PathParam("path") PropertiesPath path, @QueryParam("revision") Option<RevisionNumber> revisionNumber) {
        return modelOf(path, revisionNumber).add(PropertiesModule.MODEL_NAME, PLAIN_NAME);
    }

    @POST
    @Path("{path:.+$}")
    public Redirect post(@PathParam("path") PropertiesPath path, @FormParam("properties") PropertiesInput propertiesInput) {
        repository.put(path, properties(propertiesInput));
        return redirect(resource(ChangesResource.class).get(path, some(currentRevisionNumber.current())));
    }

    private Model modelOf(PropertiesPath path, Option<RevisionNumber> revisionNumber) {
        final Model model = basicModelOf(path, revisionNumber);
        model.
                add("revisionNumber", revisionNumber.getOrNull()).
                add("latestRevisionUrl", basePath + urlOf(resource(PropertiesResource.class).getProperties(path)));
        return model;
    }

    private Model basicModelOf(PropertiesPath path, Option<RevisionNumber> revisionNumber) {
        if (revisionNumber.isEmpty()) {
            return ModelOfProperties.modelOfProperties(path, repository.get(path));
        } else {
            return ModelOfProperties.modelOfProperties(path, repository.getAtRevision(path, revisionNumber.get()));
        }
    }

    private String modelName(Option<RevisionNumber> revisionNumber) {
        return revisionNumber.isEmpty() ? HTML_EDITABLE : HTML_READ_ONLY;
    }

}
