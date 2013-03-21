package com.googlecode.utterlyidle.authentication.api;

import com.googlecode.totallylazy.Either;
import com.googlecode.utterlyidle.Request;

public interface Authenticator {
    Either<Denial,Identity> authenticate(Username username, Password password, Request request);
}
