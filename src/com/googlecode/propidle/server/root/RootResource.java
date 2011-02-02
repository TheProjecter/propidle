package com.googlecode.propidle.server.root;

import static com.googlecode.utterlyidle.proxy.Resource.redirect;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
import com.googlecode.propidle.server.filenames.FileNamesResource;
import com.googlecode.utterlyidle.Response;

import static com.googlecode.propidle.PropertiesPath.propertiesPath;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path("/")
@Produces(TEXT_HTML)
public class RootResource {
    @GET
    public Response get(){
        return redirect(resource(FileNamesResource.class).getChildrenOf(propertiesPath("/")));
    }
}
