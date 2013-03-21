package com.googlecode.utterlyidle.authentication.api;

import com.googlecode.totallylazy.time.Clock;

public class SessionLifespan {
    private final SessionTimeout sessionTimeout;
    private Clock timeSource;

    public SessionLifespan(SessionTimeout sessionTimeout, Clock timeSource) {
        this.sessionTimeout = sessionTimeout;
        this.timeSource = timeSource;
    }

    public boolean isExceeded(AccessTime lastAccessTime) {
        return lastAccessTime!= null && timedout(lastAccessTime);
    }

    private boolean timedout(AccessTime lastAccessTime) {
        return secondsSinceLastAccess(lastAccessTime) > sessionTimeout.value();
    }

    private long secondsSinceLastAccess(AccessTime lastAccessTime) {
        return (timeSource.now().getTime() - lastAccessTime.value())/1000;
    }
}
