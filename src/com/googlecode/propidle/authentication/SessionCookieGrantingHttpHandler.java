package com.googlecode.propidle.authentication;

import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.cookies.Cookie;

import static com.googlecode.utterlyidle.HttpHeaders.*;
import static com.googlecode.utterlyidle.RequestBuilder.modify;
import static com.googlecode.utterlyidle.cookies.Cookie.cookie;
import static com.googlecode.utterlyidle.cookies.CookieParameters.cookies;
import static com.googlecode.utterlyidle.cookies.CookieParameters.toHttpHeader;
import static java.util.UUID.randomUUID;

public class SessionCookieGrantingHttpHandler implements HttpHandler {
    public static final String SESSION_COOKIE_NAME = "utterly-idle-session";
    private final HttpHandler handler;

    public SessionCookieGrantingHttpHandler(HttpHandler handler) {
        this.handler = handler;
    }

    public Response handle(Request request) throws Exception {
        if (hasSessionCookie(request)) {
            return handler.handle(request);
        }

        return handleWithNewSessionCookie(request);
    }

    private Response handleWithNewSessionCookie(Request request) throws Exception {
        Cookie newCookie = cookie(SESSION_COOKIE_NAME, randomUUID().toString());
        Response response = handler.handle(assignSessionCookie(newCookie, request));
        return assignSessionCookie(newCookie, response);
    }

    private Response assignSessionCookie(Cookie cookie, Response response) {
        return ResponseBuilder.modify(response).cookie(cookie).build();
    }

    private Request assignSessionCookie(Cookie cookie, Request request) {
        return modify(request).cookie(cookie).build();
    }

    private boolean hasSessionCookie(Request request) {
        return cookies(request.headers()).contains(SESSION_COOKIE_NAME);
    }
}
