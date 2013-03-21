package com.googlecode.propidle.authentication.api;


import com.googlecode.propidle.util.tinytype.StringTinyType;

public class SessionId extends StringTinyType<SessionId> {

    public SessionId(String value) {
        super(value);
    }

    public static SessionId sessionId(String value) {
         return new SessionId(value);
    }
}
