package com.googlecode.propidle.urls;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.*;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;
import static com.googlecode.utterlyidle.Responses.response;

public class StubHandler implements HttpHandler {
    private Request lastRequest;
    private Response response;

    public static StubHandler stubHandler(Status status) {
        return new StubHandler(response(status));
    }

    public static StubHandler stubHandler(String response) {
        return new StubHandler(response(Status.OK, sequence(Pair.<String, String>pair(CONTENT_TYPE, TEXT_PLAIN)), response.getBytes()));
    }

    public StubHandler(Response response) {
        this.response = response;
    }

    public Response handle(Request request) throws Exception {
        lastRequest = request;
        return response;
    }

    public Uri uri() {
        return ensureLastRequest().uri();
    }

    public HeaderParameters headers() {
        return ensureLastRequest().headers();
    }

    private Request ensureLastRequest() {
        if (lastRequest == null) throw new NullPointerException("Handler has not yet seen any requests");
        return lastRequest;
    }
}
