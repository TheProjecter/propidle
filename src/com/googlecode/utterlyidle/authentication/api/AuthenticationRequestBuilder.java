package com.googlecode.utterlyidle.authentication.api;

import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.Request;

public interface AuthenticationRequestBuilder {
    Request buildAuthenticationRequest(Option<Request> originatingRequest, Option<Integer> failedLoginCount);
}
