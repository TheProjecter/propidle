package com.googlecode.utterlyidle.authentication.requests;

import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.authentication.application.TestApplication;
import com.googlecode.utterlyidle.authentication.application.TestResource;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.authentication.AuthorisationTest.redirectorFor;

public class GetRequest implements Callable<Response> {
    private final TestApplication testApplication;

    public GetRequest(TestApplication testApplication) {
        this.testApplication = testApplication;
    }

    public Response call() throws Exception {
        return testApplication.handle(get(redirectorFor(TestResource.class).uriOf(method(on(TestResource.class).getHello()))).build());
    }
}
