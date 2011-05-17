package com.googlecode.propidle.requesthandlers;

import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.io.Url;

import com.googlecode.utterlyidle.MediaType;

import static com.googlecode.utterlyidle.Responses.response;

public class StubHandler implements HttpHandler {
    private Request lastRequest;
    private Response response;

    public static StubHandler stubHandler(Status status) {
        return new StubHandler(response().
                status(status));
    }

    public static StubHandler stubHandler(String response) {
        return new StubHandler(response().
                status(Status.OK).
                header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN).
                bytes(response.getBytes()));
    }

    public static StubHandler stubHandler() {
        return stubHandler("");
    }

    public StubHandler(Response response) {
        this.response = response;
    }

    public Response handle(Request request) throws Exception {
        lastRequest = request;
        return response;
    }

    public Url url() {
        return ensureLastRequest().url();
    }

    public HeaderParameters headers() {
        return ensureLastRequest().headers();
    }

    private Request ensureLastRequest() {
        if (lastRequest == null) throw new NullPointerException("Handler has not yet seen any requests");
        return lastRequest;
    }
}
