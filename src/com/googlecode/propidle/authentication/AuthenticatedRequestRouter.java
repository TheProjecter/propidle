package com.googlecode.propidle.authentication;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.RequestBuilder;

import java.io.IOException;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.RequestBuilder.get;

public class AuthenticatedRequestRouter {
    private final Base64RequestEncoding base64RequestEncoding;

    public AuthenticatedRequestRouter(Base64RequestEncoding base64RequestEncoding) {
        this.base64RequestEncoding = base64RequestEncoding;
    }

    public Request whereToGo(Request thisRequest, Option<String> originatingRequestAsString) throws IOException {
        return decode(originatingRequestAsString) == null ? redirectToRoot(thisRequest) : decode(originatingRequestAsString);
    }

    private Request redirectToRoot(Request request) {
        return sequence(request.headers()).fold(get("/"), headers()).build();
    }

    private Callable2<RequestBuilder, Pair<String, String>, RequestBuilder> headers() {
        return new Callable2<RequestBuilder, Pair<String, String>, RequestBuilder>() {
            public RequestBuilder call(RequestBuilder requestBuilder, Pair<String, String> header) throws Exception {
                requestBuilder.header(header.first(), header.second());
                return requestBuilder;
            }
        };
    }

    private Request decode(Option<String> originatingRequestAsString) throws IOException {
        return originatingRequestAsString.isEmpty() ? null : base64RequestEncoding.decode(originatingRequestAsString.get());
    }
}
