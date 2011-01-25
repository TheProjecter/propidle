package com.googlecode.propidle.requesthandlers;

import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;

public class SetStatusCode implements HttpHandler {
    private final Status status;

    public static SetStatusCode setStatusCode(Status status) {
        return new SetStatusCode(status);
    }
    public SetStatusCode(Status status) {
        this.status = status;
    }

    public void handle(Request request, Response response) throws Exception {
        response.status(status);
    }
}
