package com.googlecode.propidle.server;

import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.QueryParameters;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;


public class ConvertRevisionNumberQueryParameterToHeader implements HttpHandler {
    public static final String REVISION_PARAM = "revision";
    private final HttpHandler decorated;

    public ConvertRevisionNumberQueryParameterToHeader(HttpHandler decorated) {
        this.decorated = decorated;
    }

    public Response handle(Request request) throws Exception {
        final QueryParameters queryParameters = QueryParameters.parse(request.uri().query());
        String revision = queryParameters.getValue(REVISION_PARAM);
        if(revision != null){
            queryParameters.remove(REVISION_PARAM);
            request.headers().add(REVISION_PARAM, revision);
        }
        return decorated.handle(request);
    }
}
