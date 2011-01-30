package com.googlecode.propidle.requesthandlers;

import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.HeaderParameters;
import com.googlecode.utterlyidle.io.Url;

public class RememberRequest implements HttpHandler{
    private Request lastRequest;

    public static RememberRequest rememberRequest() {
        return new RememberRequest();
    }

    public void handle(Request request, Response response) throws Exception {
        lastRequest = request;
    }

    public Url url() {
        return ensureLastRequest().url();
    }

    public HeaderParameters headers() {
        return ensureLastRequest().headers();
    }

    private Request ensureLastRequest() {
        if(lastRequest==null) throw new NullPointerException("Handler has not yet seen any requests");
        return lastRequest;
    }
}
