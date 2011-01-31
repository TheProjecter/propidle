package com.googlecode.propidle.server.sessions;

import com.googlecode.propidle.util.TinyType;
import com.googlecode.propidle.util.StringTinyType;

import java.util.UUID;

public class SessionId extends StringTinyType<SessionId> {
    public static SessionId sessionId() {
        return sessionId(UUID.randomUUID().toString());
    }
    public static SessionId sessionId(TinyType<?,?> value) {
        return sessionId(value.toString());
    }

    public static SessionId sessionId(String value) {
        return new SessionId(value);
    }

    protected SessionId(String value) {
        super(value);
    }
}
