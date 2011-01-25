package com.googlecode.propidle.server.steps;

import com.googlecode.propidle.server.sessions.SessionId;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.MemoryResponse;

public class WebClient {
    private final Application application;
    private SessionId currentSession;
    private Response lastResponse;

    public WebClient(Application application) {
        this.application = application;
    }

    public Response handle(RequestBuilder request) throws Exception {
        if (currentSession != null) {
            request.withHeader("cookies", "session=" + currentSession.value());
        }
        Response response = new MemoryResponse();
        application.handle(request.build(), response);
        lastResponse = response;
        return response;
    }

    public Response currentPage(){
        return lastResponse;
    }

    public SessionId currentSession() {
        return currentSession;
    }

    public WebClient currentSession(SessionId sessionId) {
        this.currentSession = sessionId;
        return this;
    }
}
