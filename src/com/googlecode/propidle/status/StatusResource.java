package com.googlecode.propidle.status;


import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;
import com.googlecode.utterlyidle.rendering.Model;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;

@Path(StatusResource.NAME)
@Produces(TEXT_HTML)
public class StatusResource {

    public static final String NAME = "status";
    private StatusChecks statusChecks;

    public StatusResource(StatusChecks statusChecks) {
        this.statusChecks = statusChecks;
    }

    @GET
    public Model reportStatus() {
        return modelWithName(NAME).add("checks", statusChecks.checks());
    }
}
