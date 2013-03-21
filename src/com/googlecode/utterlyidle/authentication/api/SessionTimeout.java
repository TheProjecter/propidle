package com.googlecode.utterlyidle.authentication.api;


import com.googlecode.utterlyidle.util.tinytype.LongTinyType;

public class SessionTimeout extends LongTinyType<SessionTimeout> {

    public SessionTimeout(Long value) {
        super(value);
    }

    public static SessionTimeout sessionTimeout(Long value) {
         return new SessionTimeout(value);
    }
}
