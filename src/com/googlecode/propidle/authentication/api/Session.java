package com.googlecode.propidle.authentication.api;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.Request;

import static com.googlecode.propidle.authentication.SessionCookieGrantingHttpHandler.SESSION_COOKIE_NAME;
import static com.googlecode.propidle.authentication.api.SessionId.sessionId;
import static com.googlecode.utterlyidle.cookies.CookieParameters.cookies;

public class Session {
    private final SessionRepository sessionRepository;
    private final SessionLifespan sessionLifespan;

    public Session(SessionRepository sessionRepository, SessionLifespan sessionLifespan) {
        this.sessionRepository = sessionRepository;
        this.sessionLifespan = sessionLifespan;
    }

    public Request linkToSession(Identity identity, Request request) {
        if(hasSessionId(request)) {
            touch(request);
            sessionRepository.put(sessionIdFrom(request), identity);
        }

        return request;
    }

    public Identity identify(Request request){
        return invalidateOrCall(request, null, new Callable1<Request, Identity>() {
            public Identity call(Request request) throws Exception {
                return sessionRepository.identify(sessionIdFrom(request));
            }
        });
    }

    public boolean isAlive(Request request){
        return invalidateOrCall(request, false, new Callable1<Request, Boolean>() {
            public Boolean call(Request request) throws Exception {
                AccessTime accessTime = sessionRepository.lastAccessTimeOf(sessionIdFrom(request));
                return !sessionLifespan.isExceeded(accessTime);
            }
        });
    }

    private void touch(Request request) {
        sessionRepository.updateAccessTime(sessionIdFrom(request));
    }

    private boolean hasSessionId(Request request) {
        return sessionIdFrom(request) != null;
    }

    private SessionId sessionIdFrom(Request request) {
        String value = cookies(request.headers()).getValue(SESSION_COOKIE_NAME);
        return value == null ? null : sessionId(value);
    }

    private <T> T invalidateOrCall(Request request, T responseOnInvalidation, Callable1<Request, T> callable) {
        if(!hasSessionId(request)) {
            return responseOnInvalidation;
        }
        if (sessionLifespan.isExceeded(lastTouchTime(request))) {
            invalidate(request);
            return responseOnInvalidation;
        }
        try {
            touch(request);
            return callable.call(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invalidate(Request request) {
        sessionRepository.invalidate(sessionIdFrom(request));
    }

    private AccessTime lastTouchTime(Request request) {
        return sessionRepository.lastAccessTimeOf(sessionIdFrom(request));
    }

}
