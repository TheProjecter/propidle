package com.googlecode.propidle.properties;

import com.googlecode.propidle.filenames.FileNamesResource;
import com.googlecode.propidle.server.RequestedRevisionNumber;
import com.googlecode.propidle.versioncontrol.changes.ChangesResource;
import com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbers;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.Priority;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.rendering.Model;

import javax.ws.rs.*;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.ModelName.name;
import static com.googlecode.propidle.properties.ModelOfProperties.modelOfProperties;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.server.RequestedRevisionNumber.requestedRevisionNumber;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.utterlyidle.proxy.Resource.*;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path(PropertiesResource.NAME)
@Produces(TEXT_HTML)
public class PropertiesResource {
    private final AllProperties repository;
    private final BasePath basePath;
    private final Option<RequestedRevisionNumber> requestedRevisionNumber;
    private final HighestRevisionNumbers highestRevisionNumbers;
    public static final String NAME = "properties";
    public static final String PLAIN_NAME = NAME + ".properties";
    public static final String HTML_EDITABLE = NAME + ".html";
    public static final String HTML_READ_ONLY = NAME + ".readonly.html";

    public PropertiesResource(AllProperties repository, BasePath basePath, Option<RequestedRevisionNumber> requestedRevisionNumber, HighestRevisionNumbers highestRevisionNumbers) {
        this.repository = repository;
        this.basePath = basePath;
        this.requestedRevisionNumber = requestedRevisionNumber;
        this.highestRevisionNumbers = highestRevisionNumbers;
    }

    @GET
    public Response getAll() {
        return redirect(resource(FileNamesResource.class).getChildrenOf(propertiesPath("/")));
    }

    @GET
    public Response create(@QueryParam("path") PropertiesPath path) {
        return redirect(resource(PropertiesResource.class).getHtml(path));
    }

    @GET
    @Path("{path:.+$}")
    @Priority(Priority.High)
    public Model getHtml(@PathParam("path") PropertiesPath path) {
        Model model = modelOf(path).add("changesUrl", basePath + urlOf(resource(ChangesResource.class).get(path, none(RevisionNumber.class))));
        return name(model, modelName());
    }

    @GET
    @Path("{path:.+$}")
    @Produces(TEXT_PLAIN)
    public Model getProperties(@PathParam("path") PropertiesPath path) {
        return name(modelOf(path), PLAIN_NAME);
    }

    @POST
    @Path("{path:.+$}")
    public Response post(@PathParam("path") PropertiesPath path, @FormParam("properties") PropertiesInput propertiesInput) {
        RevisionNumber revisionNumber = repository.put(path, properties(propertiesInput));
        return redirect(resource(ChangesResource.class).get(path, some(revisionNumber)));
    }

    private Model modelOf(PropertiesPath path) {
        final Model model = basicModelOf(path);
        model.
                add("latestRevisionUrl", basePath + urlOf(resource(PropertiesResource.class).getProperties(path)));
        return model;
    }

    private Model basicModelOf(PropertiesPath path) {
        RevisionNumber revisionNumber = requestedRevisionNumber.getOrElse(highestExistingRevision());
        return modelOfProperties(path, repository.get(path, revisionNumber)).
                add("revisionNumber", revisionNumber);
    }

    private Callable<RequestedRevisionNumber> highestExistingRevision() {
        return new Callable<RequestedRevisionNumber>() {
            public RequestedRevisionNumber call() throws Exception {
                return requestedRevisionNumber(highestRevisionNumbers.highestExistingRevision());
            }
        };
    }


    private String modelName() {
        return requestedRevisionNumber.isEmpty() ? HTML_EDITABLE : HTML_READ_ONLY;
    }
}