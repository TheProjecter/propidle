package com.googlecode.propidle.root;

import com.googlecode.propidle.filenames.FileNamesResource;
import com.googlecode.utterlyidle.Redirector;
import com.googlecode.utterlyidle.Response;

import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;

import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Produces;

import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;

@Path("/")
@Produces(TEXT_HTML)
public class RootResource {

    private final Redirector redirector;

    public RootResource(Redirector redirector) {
        this.redirector = redirector;
    }

    @GET
    public Response get(){
        return redirector.seeOther(method(on(FileNamesResource.class).getChildrenOf(propertiesPath("/"))));
    }
}
