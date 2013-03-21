package com.googlecode.utterlyidle.authentication;

import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.authentication.api.Identity;
import com.googlecode.utterlyidle.authentication.api.Session;
import com.googlecode.yadic.Container;

public class InjectIdentityHandler implements HttpHandler {
    private final HttpHandler decoratedHandler;
    private final Session session;
    private final Container container;

    public InjectIdentityHandler(HttpHandler decoratedHandler, Session session, Container container){
        this.decoratedHandler = decoratedHandler;
        this.session = session;
        this.container = container;
    }

    @Override
    public Response handle(Request request) throws Exception {
        if(container.contains(Identity.class)) {
            container.remove(Identity.class);
        }
        container.addInstance(Identity.class, session.identify(request));
        return decoratedHandler.handle(request);
    }
}
