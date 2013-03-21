package com.googlecode.propidle.authentication.api;

public interface SessionRepository {
    void put(SessionId sessionId, Identity identity);

    Identity identify(SessionId sessionId);

    void invalidate(SessionId sessionId);

    AccessTime lastAccessTimeOf(SessionId sessionId);

    void updateAccessTime(SessionId sessionId);
}
