package com.googlecode.propidle.authentication.application;

import com.googlecode.utterlyidle.Request;
import com.googlecode.propidle.authentication.api.Identity;
import com.googlecode.propidle.authentication.api.Session;
import com.googlecode.propidle.authorisation.Authoriser;


public class TestAuthoriser implements Authoriser{

    private final Session session;

    public TestAuthoriser(Session session) {
        this.session = session;
    }

    public boolean authorise(Request request) {
        return isPrivate(request) ? isPermitted(request) : true; 
    }

    private boolean isPermitted(Request request) {
        Identity identity = session.identify(request);
        return identity != null && identity.toString().equals("admin");
    }

    private boolean isPrivate(Request request) {
        return request.uri().toString().endsWith("private");
    }
}
