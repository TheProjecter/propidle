package com.googlecode.propidle.properties;

import com.googlecode.propidle.PropidlePath;
import com.googlecode.propidle.filenames.FileNamesResource;
import com.googlecode.propidle.server.RequestedRevisionNumber;
import com.googlecode.propidle.versioncontrol.changes.ChangeDetails;
import com.googlecode.propidle.versioncontrol.changes.ChangesResource;
import com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbers;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.proxy.Invocation;
import com.googlecode.utterlyidle.Redirector;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.annotations.*;
import com.googlecode.utterlyidle.rendering.Model;

import java.util.concurrent.Callable;

import static com.googlecode.propidle.ModelName.name;
import static com.googlecode.propidle.properties.ModelOfProperties.modelOfProperties;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.server.RequestedRevisionNumber.requestedRevisionNumber;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Produces(TEXT_HTML)
public class PropertiesResource {
    private final AllProperties repository;
    private final Option<RequestedRevisionNumber> requestedRevisionNumber;
    private final HighestRevisionNumbers highestRevisionNumbers;
    private final Redirector redirector;
    private final PropidlePath propidlePath;
    public static final String NAME = "properties";
    public static final String PLAIN_NAME = NAME + ".properties";
    public static final String HTML_EDITABLE = NAME + ".html";
    public static final String HTML_READ_ONLY = NAME + ".readonly.html";

    public PropertiesResource(AllProperties repository, Option<RequestedRevisionNumber> requestedRevisionNumber, HighestRevisionNumbers highestRevisionNumbers, Redirector redirector, PropidlePath propidlePath, ChangeDetails changeDetails) {
        this.repository = repository;
        this.requestedRevisionNumber = requestedRevisionNumber;
        this.highestRevisionNumbers = highestRevisionNumbers;
        this.redirector = redirector;
        this.propidlePath = propidlePath;
    }

    @GET
    @Path(PropertiesResource.NAME)
    public Response getAll() {
        return redirector.seeOther(method(on(FileNamesResource.class).getChildrenOf(propertiesPath("/"))));
    }

    @GET
    @Path(PropertiesResource.NAME)
    public Response create(@QueryParam("path") PropertiesPath path) {
        return redirector.seeOther(method(on(PropertiesResource.class).getHtml(path)));
    }

    @GET
    @Path(PropertiesResource.NAME+"{path:.+$}")
    @Priority(Priority.High)
    public Model getHtml(@PathParam("path") PropertiesPath path) {
        Model model = modelOf(path).add("changesUrl", changeUrlAsModel(path));
        return name(model, modelName());
    }

    private Model changeUrlAsModel(PropertiesPath path) {
        return model().add("name", propidlePath.path(changeResourceInvocation(path))).add("url", propidlePath.absoluteUriOf(changeResourceInvocation(path)));
    }

    private Invocation<Object, Model> changeResourceInvocation(PropertiesPath path) {
        return method(on(ChangesResource.class).get(path, none(RevisionNumber.class)));
    }

    @GET
    @Path(PropertiesResource.NAME+"{path:.+$}")
    @Produces(TEXT_PLAIN)
    public Model getProperties(@PathParam("path") PropertiesPath path) {
        return name(modelOf(path), PLAIN_NAME);
    }

    @POST
    @Path(PropertiesResource.NAME+"{path:.+$}")
    public Response post(@PathParam("path") PropertiesPath path, @FormParam("properties") PropertiesInput propertiesInput) {
        RevisionNumber revisionNumber = repository.put(path, properties(propertiesInput));
        //add a row to the revNo-User Table  with(revisionNumber)
        return redirector.seeOther(method(on(ChangesResource.class).get(path, some(revisionNumber))));
    }

    private Model modelOf(PropertiesPath path) {
        final Model model = basicModelOf(path);
        model.
                add("latestRevisionUrl", propidlePath.absoluteUriOf(method(on(PropertiesResource.class).getProperties(path))));
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