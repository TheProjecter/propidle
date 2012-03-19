package com.googlecode.propidle.status;


import com.googlecode.totallylazy.Predicate;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;
import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;
import com.googlecode.utterlyidle.rendering.Model;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.HeaderParameters.headerParameters;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;
import static com.googlecode.utterlyidle.MediaType.WILDCARD;
import static com.googlecode.utterlyidle.Responses.response;
import static com.googlecode.utterlyidle.Status.*;

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
        return response(OK).entity(modelWithName(NAME).add("checks", statusChecks.checks()));
    }

}
