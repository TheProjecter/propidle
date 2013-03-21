package com.googlecode.utterlyidle.authentication.application;

import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.authentication.api.AccessControl;
import com.googlecode.utterlyidle.authentication.api.Session;

public class TestAccessControl implements AccessControl {
    private final Session sessioniser;

    public TestAccessControl(Session sessioniser) {
        this.sessioniser = sessioniser;
    }

    public boolean requiresAuthentication(Request request) {
        return isPost(request) && !sessioniser.isAlive(request);
    }

    private boolean isPost(Request request) {
        return request.method().equals("POST");
    }
}
