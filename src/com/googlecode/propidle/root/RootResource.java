package com.googlecode.propidle.root;

import static com.googlecode.utterlyidle.proxy.Resource.redirect;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
import com.googlecode.propidle.filenames.FileNamesResource;
import com.googlecode.utterlyidle.Response;

import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path("/")
@Produces(TEXT_HTML)
public class RootResource {
    @GET
    public Response get(){
        return redirect(resource(FileNamesResource.class).getChildrenOf(propertiesPath("/")));
    }
}
