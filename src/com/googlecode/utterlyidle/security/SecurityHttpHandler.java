package com.googlecode.utterlyidle.security;

import com.googlecode.totallylazy.*;
import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.authentication.AuthenticatedRequestRouter;
import com.googlecode.utterlyidle.authentication.AuthenticationResource;
import com.googlecode.utterlyidle.authentication.api.*;
import com.googlecode.utterlyidle.authentication.api.Identity;
import com.googlecode.utterlyidle.authorisation.Authoriser;
import com.googlecode.utterlyidle.authorisation.AuthorisingHttpHandler;
import com.googlecode.utterlyidle.handlers.ResponseHandlersFinder;

import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.FormParameters.parse;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.authentication.AuthenticationResource.ORIGINATING_REQUEST_FORM_NAME;
import static com.googlecode.utterlyidle.authentication.AuthenticationResource.UTTERLYIDLE_AUTHENTICATE;
import static com.googlecode.utterlyidle.authentication.api.Denial.denial;

public class SecurityHttpHandler implements HttpHandler {
    private final HttpHandler httpHandler;
    private final AccessControl accessControl;
    private final Authenticator authenticator;
    private final Session session;
    private final Redirector redirector;
    private final AuthenticatedRequestRouter authenticatedRequestRouter;
    private final AuthenticationRequestBuilder authenticationRequestBuilder;


    public SecurityHttpHandler(Redirector redirector, HttpHandler httpHandler, AccessControl accessControl, Authenticator authenticator, Session session, Authoriser authoriser, ResponseHandlersFinder responseHandlersFinder, AuthenticatedRequestRouter authenticatedRequestRouter, AuthenticationRequestBuilder authenticationRequestBuilder) {
        this.redirector = redirector;
        this.authenticatedRequestRouter = authenticatedRequestRouter;
        this.authenticationRequestBuilder = authenticationRequestBuilder;
        this.httpHandler = new AuthorisingHttpHandler(httpHandler, authoriser, responseHandlersFinder);
        this.accessControl = accessControl;
        this.authenticator = authenticator;
        this.session = session;
    }

    public Response handle(Request request) throws Exception {
        if (isPostingOfCredentials(request)) {
            return loginAndHandle(request, optionalHiddenRequest(request));
        }
        if (accessControl.requiresAuthentication(request)) {
            return httpHandler.handle(authenticationRequestBuilder.buildAuthenticationRequest(some(request), None.<Integer>none()));
        }
        return httpHandler.handle(request);
    }

    private Response loginAndHandle(final Request request, final Option<String> originatingRequestAsString) throws Exception {
        if(username(request).value().isEmpty()) {
            return denied(request, originatingRequestAsString).call(denial(username(request).value()));
        }

        final Either<Denial, Identity> authenticationResult = authenticator.authenticate(username(request), password(request), request);
        return authenticationResult.map(denied(request,originatingRequestAsString), authenticated(request, originatingRequestAsString));
    }

    private Callable1<Identity, Response> authenticated(final Request request, final Option<String> originatingRequestAsString) {
        return new Callable1<Identity, Response>() {
            public Response call(Identity identity) throws Exception {
                return httpHandler.handle(session.linkToSession(identity, authenticatedRequestRouter.whereToGo(request, originatingRequestAsString)));
            }
        };
    }

    private Callable1<Denial, Response> denied(final Request request,final Option<String> originatingRequestAsString) {
        return new Callable1<Denial, Response>() {
            public Response call(Denial denial) throws Exception {
                RequestBuilder builder = get(redirector.uriOf(method(on(AuthenticationResource.class).authenticationPage(request, originatingRequestAsString, Some.some(1)))));
                return httpHandler.handle(builder.build());
            }
        };
    }

    private Option<String> optionalHiddenRequest(Request request) {
        final FormParameters formParameters = formParameters(request);
        return formParameters.contains(ORIGINATING_REQUEST_FORM_NAME) ? Some.some(formParameters.getValue(ORIGINATING_REQUEST_FORM_NAME)) : None.<String>none();
    }

    private boolean isPostingOfCredentials(Request request) {
        return request.uri().toString().endsWith(UTTERLYIDLE_AUTHENTICATE);
    }

    private Password password(Request request) {
        return Password.password(formParameters(request).getValue("password"));
    }

    private Username username(Request request) {
        return Username.username(formParameters(request).getValue("username"));
    }

    private FormParameters formParameters(Request request) {
        return parse(request.entity());
    }
}
