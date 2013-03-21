package com.googlecode.propidle.authentication;

import com.googlecode.propidle.authentication.api.AuthenticationRequestBuilder;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.Redirector;
import com.googlecode.utterlyidle.Request;
import com.googlecode.propidle.authentication.api.AuthenticationRequestBuilder;

import java.io.IOException;

import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.propidle.authentication.SessionCookieGrantingHttpHandler.SESSION_COOKIE_NAME;
import static com.googlecode.utterlyidle.cookies.Cookie.cookie;
import static com.googlecode.utterlyidle.cookies.CookieParameters.cookies;

public class DefaultAuthenticationRequestBuilder implements AuthenticationRequestBuilder {

    private final Redirector redirector;
    private final Base64RequestEncoding encoding;

    public DefaultAuthenticationRequestBuilder(Redirector redirector, Base64RequestEncoding encoding) {
        this.redirector = redirector;
        this.encoding = encoding;
    }

    public Request buildAuthenticationRequest(Option<Request> originatingRequest, Option<Integer> failedLoginCount) {

        Option<String> encodedRequest = originatingRequest.map(encodedRequest());
        String sessionCookie = originatingRequest.map(sessionCookie()).getOrNull();

        try {
            return get(redirector.uriOf(method(on(AuthenticationResource.class).authenticationPage(null, encodedRequest, failedLoginCount)))).cookie(cookie(SessionCookieGrantingHttpHandler.SESSION_COOKIE_NAME, sessionCookie)).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Callable1<Request, String> sessionCookie() {
        return new Callable1<Request, String>() {
            public String call(Request request) throws Exception {
                return cookies(request.headers()).getValue(SessionCookieGrantingHttpHandler.SESSION_COOKIE_NAME);
            }
        };
    }

    private Callable1<Request, String> encodedRequest() {
        return new Callable1<Request, String>() {
            public String call(Request request) throws Exception {
                return encoding.encode(request);
            }
        };
    }
}
