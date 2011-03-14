package com.googlecode.propidle.monitoring;

import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;

public class HttpRequestCountingHandler implements HttpHandler {
    private final HttpHandler httpHandler;
    private final HttpRequestCounter counter;

    public HttpRequestCountingHandler(HttpHandler httpHandler, HttpRequestCounter counter) {
        this.httpHandler = httpHandler;
        this.counter = counter;
    }

    public Response handle(Request request) throws Exception {
        counter.increment();
        return httpHandler.handle(request);
    }
}
