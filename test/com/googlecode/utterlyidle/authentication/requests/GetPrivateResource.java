package com.googlecode.utterlyidle.authentication.requests;

import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.authentication.application.PrivateResource;
import com.googlecode.utterlyidle.authentication.application.TestApplication;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.authentication.AuthorisationTest.redirectorFor;

public class GetPrivateResource implements Callable<Response> {
    private final TestApplication testApplication;

    public GetPrivateResource(TestApplication testApplication) {
        this.testApplication = testApplication;
    }

    public Response call() throws Exception {
        return testApplication.handle(get(redirectorFor(PrivateResource.class).uriOf(method(on(PrivateResource.class).secretMessage()))).build());
    }

}
