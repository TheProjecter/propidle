package com.googlecode.propidle.authentication.application;

import com.googlecode.propidle.authentication.api.InMemorySessionRepository;
import com.googlecode.propidle.authentication.api.SessionRepository;
import com.googlecode.propidle.authentication.requests.ActionUrl;
import com.googlecode.propidle.security.SecurityModule;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.RestApplication;
import com.googlecode.utterlyidle.cookies.Cookie;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.authentication.SessionCookieGrantingHttpHandler.SESSION_COOKIE_NAME;
import static com.googlecode.utterlyidle.BasePath.basePath;

public class TestApplication extends RestApplication {
    private Response lastResponse;
    private Request lastRequest;
    private Cookie sessionCookie;
    private ActionUrl actionUrl;

    public TestApplication(Module... extraModules) {
        super(basePath("/"));
        sessionCookie = new Cookie(SESSION_COOKIE_NAME, UUID.randomUUID().toString());
        add(extraModules);
        add(new SecurityModule());
        add(new TestSupportModule());
    }

    private void add(Module[] extraModule) {
        for (Module module : extraModule) {
            add(module);
        }
    }

    public Response request(final Class<? extends Callable<Response>> operation) throws Exception {
        lastResponse = (Response) usingRequestScope(new Callable1<Container, Object>() {
            public Object call(Container requestScopeContainer) throws Exception {
                requestScopeContainer.addInstance(TestApplication.class, TestApplication.this);
                requestScopeContainer.addInstance(ActionUrl.class, actionUrl);
                requestScopeContainer.add(Callable.class, operation);
                return requestScopeContainer.get(Callable.class).call();
            }
        });

        return lastResponse;
    }

    public Response lastResponse() {
        return lastResponse;
    }

    @Override
    public Response handle(Request request) throws Exception {
        request = withSessionCookie(request);
        try {
            return super.handle(request);
        } finally {
            lastRequest = request;
        }
    }

    private Request withSessionCookie(Request request) {
        return new RequestBuilder(request).cookie(sessionCookie).build();
    }

    @SuppressWarnings("unchecked")
    public <T> T usingRequestScope(final Class<T> something) throws Exception {
        return usingRequestScope(new Callable1<Container, T>() {
            public T call(Container container) throws Exception {
                container.addInstance(TestApplication.class, TestApplication.this);
                return container.get(something);
            }
        });
    }


    @SuppressWarnings("unchecked")
    public <T> T perform(Callable<T> step) throws Exception {
        Container container = new SimpleContainer();
        container.addInstance(TestApplication.class, this);
        container.addInstance(Callable.class, step);

        return (T) container.get(Callable.class).call();
    }

    public Request lastRequest() {
        return lastRequest;
    }

    public void clearSessions() {
        usingRequestScope(new Callable1<Container, Void>() {
            public Void call(Container container) throws Exception {
                ((InMemorySessionRepository) container.get(SessionRepository.class)).clearSessions();
                return null;
            }
        });
    }

    public void setActionUrl(ActionUrl actionUrl) {
        this.actionUrl = actionUrl;
    }

    @Override
    public void close() throws IOException {
        // ignore
    }

    public void shutdown() throws IOException {
        super.close();
    }
}
