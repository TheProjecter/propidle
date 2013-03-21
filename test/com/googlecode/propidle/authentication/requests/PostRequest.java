package com.googlecode.propidle.authentication.requests;

import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.propidle.authentication.application.TestApplication;
import com.googlecode.propidle.authentication.application.TestResource;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.propidle.authentication.AuthorisationTest.redirectorFor;


public class PostRequest implements Callable<Response> {
    private final TestApplication testApplication;

    public PostRequest(TestApplication testApplication) {
        this.testApplication = testApplication;
    }

    public Response call() throws Exception {

        RequestBuilder builder = post(redirectorFor(TestResource.class).uriOf(method(on((TestResource.class)).getHello())));

        Request request = builder.build();

        return testApplication.handle(request);
    }
}
