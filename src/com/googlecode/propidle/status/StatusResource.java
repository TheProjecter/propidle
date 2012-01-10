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
        Iterable<StatusCheckResult> statusCheckResults = statusChecks.checks();
        Status status = Status.OK;
        if ( sequence(statusCheckResults).exists(fatalStatusCheckResult()) ){
            status = status.SERVICE_UNAVAILABLE;
        }
        return response(status).entity(modelWithName(NAME).add("checks", statusCheckResults));
    }

    private Predicate<StatusCheckResult> fatalStatusCheckResult() {
        return new Predicate<StatusCheckResult>() {
            public boolean matches(StatusCheckResult statusCheckResult) {
                return statusCheckResult.isFatal();
            }
        };
    }

}
