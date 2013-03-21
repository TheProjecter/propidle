package com.googlecode.utterlyidle.authentication.api;

import com.googlecode.totallylazy.time.Clock;
import com.googlecode.utterlyidle.authentication.api.AccessTime;
import com.googlecode.utterlyidle.authentication.api.Identity;
import com.googlecode.utterlyidle.authentication.api.SessionId;
import com.googlecode.utterlyidle.authentication.api.SessionRepository;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.utterlyidle.authentication.api.AccessTime.accessTime;
import static com.googlecode.utterlyidle.authentication.api.AccessTime.neverAccessed;

public class InMemorySessionRepository implements SessionRepository {
    private static final Map<SessionId, Identity> identities = new HashMap<SessionId, Identity>();
    private static final Map<SessionId, AccessTime> accessTimes = new HashMap<SessionId, AccessTime>();
    private Clock timeSource;

    public InMemorySessionRepository(Clock timeSource) {
        this.timeSource = timeSource;
    }

    public void put(SessionId sessionId, Identity identity) {
        identities.put(sessionId, identity);
    }

    public Identity identify(SessionId sessionid) {
        return identities.get(sessionid);
    }

    public void invalidate(SessionId sessionId) {
        identities.remove(sessionId);
    }

    public void updateAccessTime(SessionId sessionId) {
        accessTimes.put(sessionId, accessTime(timeSource.now().getTime()));
    }

    public AccessTime lastAccessTimeOf(SessionId sessionId) {
        return accessTimes.containsKey(sessionId) ? accessTimes.get(sessionId) : neverAccessed();
    }

    public void clearSessions() {
        identities.clear();
        accessTimes.clear();
    }
}
