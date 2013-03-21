package com.googlecode.utterlyidle.authentication.application;

import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.POST;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;
import com.googlecode.utterlyidle.security.SecurityModule;

import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.ResponseBuilder.response;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Path("test-resource")
@Produces(TEXT_HTML)
public class TestResource {

    @POST
    public Response postHello() {
        return response().entity(model().add(SecurityModule.MODEL_NAME, "postHello").add("posting", "hello")).build();
    }

    @GET
    public Response getHello() {
        return response().entity(model().add(SecurityModule.MODEL_NAME, "getHello").add("getting", "hello")).build();
    }
}
