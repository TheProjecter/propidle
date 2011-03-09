package com.googlecode.propidle.authentication;

import static com.googlecode.propidle.authentication.Session.session;
import static com.googlecode.propidle.authentication.SessionId.sessionId;
import static com.googlecode.propidle.authentication.StartTime.startTime;
import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.authorisation.users.Username;
import com.googlecode.propidle.authorisation.users.Password;
import com.googlecode.totallylazy.Either;
import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Right.right;

public class SessionStarter {
    private final Sessions sessions;
    private final Authenticator authenticator;
    private final Clock clock;

    public SessionStarter(Sessions sessions, Authenticator authenticator, Clock clock) {
        this.sessions = sessions;
        this.authenticator = authenticator;
        this.clock = clock;
    }

    public Either<Session, AuthenticationException> start(Username username, Password password) {
        Either<AuthenticationToken, AuthenticationException> authenticationResult = authenticator.authenticate(username, password);
        if (authenticationResult.isRight()) return right(authenticationResult.right());

        Session session = session(sessionId(authenticationResult.left()), username, StartTime.startTime(clock));

        sessions.put(session);
        return left(session);
    }
}
