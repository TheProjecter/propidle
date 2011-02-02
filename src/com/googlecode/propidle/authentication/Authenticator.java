package com.googlecode.propidle.authentication;

import com.googlecode.totallylazy.Either;
import com.googlecode.propidle.authorisation.users.Username;
import com.googlecode.propidle.authorisation.users.Password;

public interface Authenticator {
    Either<AuthenticationToken, AuthenticationException> authenticate(Username username, Password password);
}
