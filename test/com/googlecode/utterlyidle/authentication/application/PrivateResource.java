package com.googlecode.utterlyidle.authentication.application;

import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;

import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.ResponseBuilder.response;
import static com.googlecode.utterlyidle.rendering.Model.model;
import static com.googlecode.utterlyidle.security.SecurityModule.MODEL_NAME;

@Path(PrivateResource.NAME)
@Produces(TEXT_HTML)
public class PrivateResource {

    public static final String NAME = "private";

    @GET
    public Response secretMessage() {
        return response().entity(model().add(MODEL_NAME, "PrivateResource").add("message", "the secret message is 'bananas'")).build();
    }
}
