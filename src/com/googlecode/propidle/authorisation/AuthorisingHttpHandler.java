package com.googlecode.propidle.authorisation;

import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;
import com.googlecode.utterlyidle.handlers.ResponseHandlersFinder;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.Responses.response;
import static com.googlecode.utterlyidle.rendering.Model.model;

public class AuthorisingHttpHandler implements HttpHandler {
    private final HttpHandler delegate;
    private final Authoriser authoriser;
    private final ResponseHandlersFinder handlers;

    public AuthorisingHttpHandler(HttpHandler delegate, Authoriser authoriser, ResponseHandlersFinder handlers) {
        this.delegate = delegate;
        this.authoriser = authoriser;
        this.handlers = handlers;
    }

    public Response handle(Request request) throws Exception {
        if(!authoriser.authorise(request)) {
            return handlers.findAndHandle(request, response(Status.FORBIDDEN, sequence(pair(CONTENT_TYPE, TEXT_HTML)), model()));
        }
        return delegate.handle(request);
    }
}
