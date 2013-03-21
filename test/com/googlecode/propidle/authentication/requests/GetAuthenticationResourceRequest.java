package com.googlecode.propidle.authentication.requests;

import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.propidle.authentication.AuthenticationResource;
import com.googlecode.propidle.authentication.application.TestApplication;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.propidle.authentication.AuthorisationTest.redirectorFor;

public class GetAuthenticationResourceRequest implements Callable<Response> {
    private final TestApplication testApplication;

    public GetAuthenticationResourceRequest(TestApplication testApplication) {
        this.testApplication = testApplication;
    }

    public Response call() throws Exception {
        Request request = get(redirectorFor(AuthenticationResource.class).uriOf(method(on(AuthenticationResource.class).authenticationPage(RequestBuilder.get("/").build(), Option.<String>none(), Option.<Integer>none())))).build();
        return testApplication.handle(request);
    }
}
