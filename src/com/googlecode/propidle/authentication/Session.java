package com.googlecode.propidle.authentication;

import com.googlecode.propidle.authorisation.users.Username;
import static com.googlecode.propidle.authentication.SessionId.sessionId;

public class Session {
    private final SessionId id;
    private final Username username;
    private final StartTime startTime;

    public static Session session(SessionId id, Username username, StartTime startTime) {
        return new Session(id, username, startTime);
    }

    protected Session(SessionId id, Username username, StartTime startTime) {
        this.id = id;
        this.username = username;
        this.startTime = startTime;
    }

    public SessionId id() {
        return id;
    }

    public Username username() {
        return username;
    }

    public StartTime startTime() {
        return startTime;
    }

    public static Session guestSession(StartTime startTime) {
        return session(SessionId.sessionId(), Username.GUEST, startTime);
    }
}
