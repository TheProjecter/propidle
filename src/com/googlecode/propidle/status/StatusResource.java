package com.googlecode.propidle.status;


import com.googlecode.propidle.ModelName;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;
import com.googlecode.utterlyidle.rendering.Model;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.utterlyidle.HeaderParameters.headerParameters;
import static com.googlecode.utterlyidle.Responses.response;
import static com.googlecode.utterlyidle.rendering.Model.model;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path(StatusResource.NAME)
@Produces(TEXT_HTML)
public class StatusResource {

    public static final String NAME = "status";
    private StatusChecks statusChecks;

    public StatusResource(StatusChecks statusChecks) {
        this.statusChecks = statusChecks;
    }

    @GET
    public Response reportStatus() {
        return response(Status.OK, headerParameters(), modelOf(statusChecks));
    }

    private Model modelOf(StatusChecks checkList) {
        Iterable<StatusCheckResult> statusCheckResults = checkList.checks();
        return modelWithName(NAME).add("checks", statusCheckResults);
    }

}
