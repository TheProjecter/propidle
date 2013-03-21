package com.googlecode.utterlyidle.authentication.requests;

import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.FormParameters;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.authentication.AuthenticationResource;
import com.googlecode.utterlyidle.authentication.Base64RequestEncoding;
import com.googlecode.utterlyidle.authentication.application.TestApplication;

import java.util.concurrent.Callable;

import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.authentication.AuthenticationResource.ORIGINATING_REQUEST_FORM_NAME;
import static com.googlecode.utterlyidle.authentication.AuthenticationResource.redirectToAuthenticationHandler;

public class PostFromLoginForm implements Callable<Response> {
    protected final TestApplication testApplication;
    private final String username;
    private final String password;
    private final Base64RequestEncoding base64RequestEncoding;
    private final ActionUrl actionUrl;

    public PostFromLoginForm(TestApplication testApplication, String username, String password, Base64RequestEncoding base64RequestEncoding, ActionUrl actionUrl) {
        this.testApplication = testApplication;
        this.username = username;
        this.password = password;
        this.base64RequestEncoding = base64RequestEncoding;
        this.actionUrl = actionUrl;
    }

    public Response call() throws Exception {
        Request request = post(redirectToAuthenticationHandler(actionUrl.uri()).toString()).build();
        request = addLoginFormParameters(request);
        request = addOriginatingRequestToForm(request);

        return testApplication.handle(request);
    }

    private Request addOriginatingRequestToForm(Request request) {
        Request originatingRequest = testApplication.lastRequest();
        if (originatingRequest != null && !isToAuthenticationResource(originatingRequest)) {
            final FormParameters formParameters = formParameters(originatingRequest);
            if (formParameters.contains(ORIGINATING_REQUEST_FORM_NAME)) {
                String value = formParameters.getValue(ORIGINATING_REQUEST_FORM_NAME);
                return new RequestBuilder(request).form(ORIGINATING_REQUEST_FORM_NAME, value).build();
            } else {
                return new RequestBuilder(request).form(ORIGINATING_REQUEST_FORM_NAME, base64RequestEncoding.encode(originatingRequest)).build();
            }
        }
        return request;
    }

    private Request addLoginFormParameters(Request request) {
        return new RequestBuilder(request).form("username", username).form("password", password).build();
    }

    private boolean isToAuthenticationResource(Request originatingRequest) {
        return originatingRequest.uri().toString().startsWith(AuthenticationResource.NAME);
    }

    private FormParameters formParameters(Request request) {
        return FormParameters.parse(request.entity());
    }
}
