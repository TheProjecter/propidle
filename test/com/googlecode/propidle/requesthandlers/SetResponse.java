package com.googlecode.propidle.requesthandlers;

import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class SetResponse implements HttpHandler {
    private final String content;

    public static SetResponse setResponse(String output) {
        return new SetResponse(output);
    }

    public SetResponse(String content) {
        this.content = content;
    }

    public void handle(Request request, Response response) throws Exception {
        response.status(Status.OK);
        response.header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN);
        response.output().write(content.getBytes());
        response.output().close();
    }
}
