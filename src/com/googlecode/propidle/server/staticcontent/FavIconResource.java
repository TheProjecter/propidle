package com.googlecode.propidle.server.staticcontent;

import com.googlecode.utterlyidle.Response;

import com.googlecode.utterlyidle.Responses;
import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;

import static com.googlecode.utterlyidle.proxy.Resource.redirect;
import static com.googlecode.utterlyidle.proxy.Resource.resource;

@Path("favicon.ico")
public class FavIconResource {
    @GET
    public Response get(){
        return Responses.seeOther("static/favicon.ico");
    }
}
