package com.googlecode.utterlyidle.authentication.application;

import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.authentication.application.TestApplication;

import java.util.concurrent.Callable;

public class LastRequest implements Callable<Request> {
    private final com.googlecode.utterlyidle.authentication.application.TestApplication testApplication;

    public LastRequest(TestApplication testApplication) {
        this.testApplication = testApplication;
    }

    public Request call() throws Exception {
        return testApplication.lastRequest();
    }
}
