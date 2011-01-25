package com.googlecode.propidle.server.root;

import com.googlecode.utterlyidle.Redirect;
import static com.googlecode.utterlyidle.proxy.Resource.redirect;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
import com.googlecode.propidle.server.filenames.FileNamesResource;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;

import javax.ws.rs.Path;
import javax.ws.rs.GET;

@Path("/")
public class RootResource {
    @GET
    public Redirect get(){
        return redirect(resource(FileNamesResource.class).getChildrenOf(propertiesPath("/")));
    }
}
