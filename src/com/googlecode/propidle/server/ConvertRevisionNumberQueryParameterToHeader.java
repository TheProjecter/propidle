package com.googlecode.propidle.server;

import com.googlecode.utterlyidle.*;

import static com.googlecode.utterlyidle.RequestBuilder.*;


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
            request = modify(request).removeQuery(REVISION_PARAM).header(REVISION_PARAM, revision).build();
        }
        return decorated.handle(request);
    }
}
