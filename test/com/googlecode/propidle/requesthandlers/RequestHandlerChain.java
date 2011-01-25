package com.googlecode.propidle.requesthandlers;

import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;

import static com.googlecode.totallylazy.Sequences.sequence;

public class RequestHandlerChain implements HttpHandler {
    private final Iterable<HttpHandler> chain;

    public static RequestHandlerChain chain(HttpHandler... chain) {
        return chain(sequence(chain));
    }
    public static RequestHandlerChain chain(Iterable<HttpHandler> chain) {
        return new RequestHandlerChain(chain);
    }

    public RequestHandlerChain(Iterable<HttpHandler> chain) {
        this.chain = chain;
    }

    public void handle(Request request, Response response) throws Exception {
        for (HttpHandler handler : chain) {
            handler.handle(request, response);
        }
    }
}
