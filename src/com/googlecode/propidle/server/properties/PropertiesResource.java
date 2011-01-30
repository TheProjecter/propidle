package com.googlecode.propidle.server.properties;

import com.googlecode.propidle.AllProperties;
import static com.googlecode.propidle.Properties.properties;
import com.googlecode.propidle.PropertiesPath;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.propidle.server.RequestedRevisionNumber;
import com.googlecode.propidle.server.changes.ChangesResource;
import com.googlecode.propidle.server.filenames.FileNamesResource;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Option;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.Redirect;
import static com.googlecode.utterlyidle.proxy.Resource.*;
import com.googlecode.utterlyidle.rendering.Model;

import javax.ws.rs.*;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.MediaType;

@Path(PropertiesResource.NAME)
@Produces(TEXT_HTML)
public class PropertiesResource {
    private final AllProperties repository;
    private final BasePath basePath;
    private final Option<RequestedRevisionNumber> requestedRevisionNumber;
    public static final String NAME = "properties";
    public static final String PLAIN_NAME = NAME + ".properties";
    public static final String HTML_EDITABLE = NAME + ".html";
    public static final String HTML_READ_ONLY = NAME + ".readonly.html";

    public PropertiesResource(AllProperties repository, BasePath basePath, Option<RequestedRevisionNumber> requestedRevisionNumber) {
        this.repository = repository;
        this.basePath = basePath;
        this.requestedRevisionNumber = requestedRevisionNumber;
    }

    @GET
    public Redirect getAll() {
        return redirect(resource(FileNamesResource.class).getChildrenOf(propertiesPath("/")));
    }

    @GET
    public Redirect create(@QueryParam("path") PropertiesPath path) {
        return redirect(resource(PropertiesResource.class).getHtml(path));
    }

    @GET
    @Path("{path:.+$}")
    public Model getHtml(@PathParam("path") PropertiesPath path) {
        return modelOf(path).
                add(PropertiesModule.MODEL_NAME, modelName()).
                add("changesUrl", basePath + urlOf(resource(ChangesResource.class).get(path, none(RevisionNumber.class))));
    }

    @GET
    @Path("{path:.+$}")
    @Produces(TEXT_PLAIN)
    public Model getProperties(@PathParam("path") PropertiesPath path) {
        return modelOf(path).add(PropertiesModule.MODEL_NAME, PLAIN_NAME);
    }

    @POST
    @Path("{path:.+$}")
    public Redirect post(@PathParam("path") PropertiesPath path, @FormParam("properties") PropertiesInput propertiesInput) {
        RevisionNumber revisionNumber = repository.put(path, properties(propertiesInput));
        return redirect(resource(ChangesResource.class).get(path, some(revisionNumber)));
    }

    private Model modelOf(PropertiesPath path) {
        final Model model = basicModelOf(path);
        model.
                add("revisionNumber", requestedRevisionNumber.getOrNull()).
                add("latestRevisionUrl", basePath + urlOf(resource(PropertiesResource.class).getProperties(path)));
        return model;
    }

    private Model basicModelOf(PropertiesPath path) {
        if (requestedRevisionNumber.isEmpty()) {
            return ModelOfProperties.modelOfProperties(path, repository.get(path));
        } else {
            return ModelOfProperties.modelOfProperties(path, repository.getAtRevision(path, requestedRevisionNumber.get()));
        }
    }

    private String modelName() {
        return requestedRevisionNumber.isEmpty() ? HTML_EDITABLE : HTML_READ_ONLY;
    }
}