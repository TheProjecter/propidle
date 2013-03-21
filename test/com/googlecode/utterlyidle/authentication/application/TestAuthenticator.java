package com.googlecode.utterlyidle.authentication.application;

import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Left;
import com.googlecode.totallylazy.Right;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.authentication.api.*;

import static com.googlecode.utterlyidle.authentication.api.Denial.*;
import static com.googlecode.utterlyidle.authentication.api.Identity.*;

public class TestAuthenticator implements Authenticator {

    public Either<Denial, Identity> authenticate(Username username, Password password, Request request) {
        boolean authenticated = username.value().equals(password.value());
        return authenticated ?  Right.<Denial, Identity>right(identity(username.value())) : Left.<Denial, Identity>left(denial(username.value()));
    }
}
