package com.googlecode.utterlyidle.security;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.authentication.*;
import com.googlecode.utterlyidle.authentication.api.Session;
import com.googlecode.utterlyidle.authentication.api.SessionLifespan;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.utterlyidle.modules.ResponseHandlersModule;
import com.googlecode.utterlyidle.modules.renderers.AuthenticationResourceResponseHandler;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.yadic.Container;

import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;

public class SecurityModule implements RequestScopedModule, ResourcesModule, ResponseHandlersModule {
    public static final String MODEL_NAME = "MODEL_NAME";

    public Container addPerRequestObjects(Container container) {
        container.add(Session.class);
        container.add(MemoryRequestRenderer.class);
        container.add(Base64RequestEncoding.class);
        container.add(SessionLifespan.class, SessionLifespan.class);
        container.add(AuthenticatedRequestRouter.class);
        container.decorate(HttpHandler.class, InjectIdentityHandler.class);
        container.decorate(HttpHandler.class, SecurityHttpHandler.class);
        return container.decorate(HttpHandler.class, SessionCookieGrantingHttpHandler.class);
    }

    public Resources addResources(Resources resources) {
        return resources.add(annotatedClass(AuthenticationResource.class));
    }

    public ResponseHandlers addResponseHandlers(ResponseHandlers responseHandlers) {
        return responseHandlers.add(where(entity(Model.class), nameIs(AuthenticationResource.NAME)), AuthenticationResourceResponseHandler.class);
    }


    public static LogicalPredicate<Model> nameIs(final String name) {
        Predicate<Model> nameMatcher = new Predicate<Model>() {
            public boolean matches(Model model) {
                return model != null && model.containsKey(SecurityModule.MODEL_NAME) && name.equals(model.first(SecurityModule.MODEL_NAME));
            }
        };
        return and(notNullValue(Model.class), nameMatcher);
    }


}
