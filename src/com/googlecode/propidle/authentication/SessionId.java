package com.googlecode.propidle.authentication;

import com.googlecode.propidle.util.tinytype.TinyType;
import com.googlecode.propidle.util.tinytype.StringTinyType;

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
