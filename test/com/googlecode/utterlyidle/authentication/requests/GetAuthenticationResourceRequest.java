package com.googlecode.utterlyidle.authentication.requests;

import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.authentication.AuthenticationResource;
import com.googlecode.utterlyidle.authentication.application.TestApplication;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.authentication.AuthorisationTest.redirectorFor;

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
