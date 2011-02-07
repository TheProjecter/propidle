package com.googlecode.propidle.authentication;

import com.googlecode.totallylazy.Option;

public interface Sessions {
    Sessions put(Session session);
    Option<Session> get(SessionId id);
}
