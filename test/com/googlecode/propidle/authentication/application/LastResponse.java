package com.googlecode.propidle.authentication.application;

import com.googlecode.utterlyidle.Response;

import java.util.concurrent.Callable;

public class LastResponse implements Callable<Response> {
    private final TestApplication testApplication;

    public LastResponse(TestApplication testApplication) {
        this.testApplication = testApplication;
    }

    public Response call() throws Exception {
        return testApplication.lastResponse();
    }
}
