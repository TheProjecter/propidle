package com.googlecode.propidle.authentication.application;

import com.googlecode.propidle.authentication.api.*;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Left;
import com.googlecode.totallylazy.Right;
import com.googlecode.utterlyidle.Request;

import static com.googlecode.propidle.authentication.api.Denial.denial;
import static com.googlecode.propidle.authentication.api.Identity.identity;

public class TestAuthenticator implements Authenticator {

    public Either<Denial, Identity> authenticate(Username username, Password password, Request request) {
        boolean authenticated = username.value().equals(password.value());
        return authenticated ?  Right.<Denial, Identity>right(identity(username.value())) : Left.<Denial, Identity>left(denial(username.value()));
    }
}
