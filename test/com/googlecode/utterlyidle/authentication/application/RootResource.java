package com.googlecode.utterlyidle.authentication.application;

import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.utterlyidle.security.SecurityModule;

import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Path("/")
@Produces(TEXT_HTML)
public class RootResource {

    @GET
    public Model getHello() {
        return model().add(SecurityModule.MODEL_NAME, "Children of '/'").add("getting", "root resource");
    }
}
