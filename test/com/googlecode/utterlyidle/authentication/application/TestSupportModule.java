package com.googlecode.utterlyidle.authentication.application;

import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.authentication.DefaultAuthenticationRequestBuilder;
import com.googlecode.utterlyidle.authentication.api.*;
import com.googlecode.utterlyidle.authorisation.Authoriser;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.utterlyidle.modules.ResponseHandlersModule;
import com.googlecode.utterlyidle.modules.renderers.ModelTemplateRenderer;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.yadic.Container;

import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static com.googlecode.utterlyidle.authentication.api.SessionTimeout.sessionTimeout;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;
import static com.googlecode.utterlyidle.security.SecurityModule.nameIs;
import static java.util.concurrent.TimeUnit.DAYS;

public class TestSupportModule implements ResourcesModule, RequestScopedModule, ResponseHandlersModule {
    public Resources addResources(Resources resources) {
        resources.add(annotatedClass(TestResource.class));
        resources.add(annotatedClass(RootResource.class));
        return resources.add(annotatedClass(PrivateResource.class));
    }

    public Container addPerRequestObjects(Container container) {
        container.add(LastRequest.class);
        container.add(LastResponse.class);
        container.add(Authenticator.class, TestAuthenticator.class);
        container.add(AccessControl.class, TestAccessControl.class);
        container.add(Authoriser.class, TestAuthoriser.class);
        container.add(SessionRepository.class, InMemorySessionRepository.class);
        container.add(AuthenticationRequestBuilder.class, DefaultAuthenticationRequestBuilder.class);
        return container.addInstance(SessionTimeout.class, sessionTimeout(DAYS.toMillis(1)));
    }

    public ResponseHandlers addResponseHandlers(ResponseHandlers handlers) {
        handlers.add(where(entity(Model.class), nameIs("getHello")), renderer(new ModelTemplateRenderer("GetHello_html", TestResource.class)));
        handlers.add(where(entity(Model.class), nameIs("postHello")), renderer(new ModelTemplateRenderer("PostHello_html", TestResource.class)));
        return handlers.add(where(entity(Model.class), nameIs(PrivateResource.class.getSimpleName())), renderer(new ModelTemplateRenderer("PrivateResource_html", PrivateResource.class)));
    }
}
