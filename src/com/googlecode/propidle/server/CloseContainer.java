package com.googlecode.propidle.server;

import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.yadic.Container;

public class CloseContainer implements HttpHandler {
    private final HttpHandler decorated;
    private final Container container;

    public CloseContainer(HttpHandler decorated, Container container) {
        this.decorated = decorated;
        this.container = container;
    }

    public Response handle(Request request) throws Exception {
        try {
            return decorated.handle(request);
        } finally {
            container.close();
        }
    }
}
