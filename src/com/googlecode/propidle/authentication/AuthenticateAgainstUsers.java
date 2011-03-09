package com.googlecode.propidle.authentication;

import static com.googlecode.propidle.authentication.AuthenticationToken.authenticationToken;
import com.googlecode.propidle.authorisation.users.*;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Right;
import com.googlecode.totallylazy.Option;
import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Right.right;

public class AuthenticateAgainstUsers implements Authenticator {
    private final Users users;
    private final PasswordHasher passwordHasher;

    public AuthenticateAgainstUsers(Users users, PasswordHasher passwordHasher) {
        this.users = users;
        this.passwordHasher = passwordHasher;
    }

    public Either<AuthenticationToken, AuthenticationException> authenticate(Username username, Password password) {
        Option<User> user = users.get(username);
        if (user.isEmpty()) return exception(username);

        PasswordHash expectedHash = passwordHasher.hash(password);

        if (!expectedHash.equals(user.get().passwordHash())) {
            return exception(username);
        } else {
            return left(authenticationToken());
        }
    }

    private Right<AuthenticationToken, AuthenticationException> exception(Username username) {
        return right(new AuthenticationException(username));
    }
}
