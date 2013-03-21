package com.googlecode.utterlyidle.authentication.api;

import com.googlecode.utterlyidle.util.tinytype.StringTinyType;

public class SessionId extends StringTinyType<SessionId> {

    public SessionId(String value) {
        super(value);
    }

    public static SessionId sessionId(String value) {
         return new SessionId(value);
    }
}
