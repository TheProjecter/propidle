package com.googlecode.utterlyidle.authentication.application;

import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.authentication.application.TestApplication;

import java.util.concurrent.Callable;

public class LastResponse implements Callable<Response> {
    private final com.googlecode.utterlyidle.authentication.application.TestApplication testApplication;

    public LastResponse(TestApplication testApplication) {
        this.testApplication = testApplication;
    }

    public Response call() throws Exception {
        return testApplication.lastResponse();
    }
}
