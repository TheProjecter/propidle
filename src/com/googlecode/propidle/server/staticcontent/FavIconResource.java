package com.googlecode.propidle.server.staticcontent;

import com.googlecode.utterlyidle.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import static com.googlecode.utterlyidle.proxy.Resource.redirect;
import static com.googlecode.utterlyidle.proxy.Resource.resource;

@Path("favicon.ico")
public class FavIconResource {
    @GET
    public Response get(){
        return redirect(resource(StaticContentResource.class).get("favicon.ico"));
    }
}
