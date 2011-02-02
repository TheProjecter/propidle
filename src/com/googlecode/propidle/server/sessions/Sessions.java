package com.googlecode.propidle.server.sessions;

import com.googlecode.totallylazy.Option;

public interface Sessions {
    Sessions put(Session session);
    Option<Session> get(SessionId id);
}
