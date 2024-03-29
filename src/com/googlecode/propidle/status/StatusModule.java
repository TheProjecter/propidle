package com.googlecode.propidle.status;

import com.googlecode.propidle.PersistenceMechanism;
import com.googlecode.propidle.server.ModelTemplateRenderer;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.utterlyidle.modules.ResponseHandlersModule;
import com.googlecode.yadic.Container;

import java.util.Properties;

import static com.googlecode.propidle.ModelName.*;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;
import static com.googlecode.utterlyidle.MediaType.WILDCARD;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;
import static com.googlecode.utterlyidle.sitemesh.ContentTypePredicate.contentType;

public class StatusModule implements RequestScopedModule, ResourcesModule, ResponseHandlersModule {

    public Container addPerRequestObjects(final Container container) {
        container.add(StatusChecks.class);
        container.get(StatusChecks.class).add(DisplayBuildNumber.class);
        container.get(StatusChecks.class).add(LuceneDirectoryCheck.class);
        PersistenceMechanism mechanism = container.get(PersistenceMechanism.class);
        if (mechanism != PersistenceMechanism.IN_MEMORY) {
            container.get(StatusChecks.class).add(ConnectionDetailsCheck.class);
            container.get(StatusChecks.class).add(DatabaseVersionCheck.class);
        }
        return container;
    }

    public Resources addResources(Resources resources) {
        return resources.add(annotatedClass(StatusResource.class));
    }

    public ResponseHandlers addResponseHandlers(ResponseHandlers handlers) {
        return handlers.add(modelNameIs(StatusResource.NAME).and(contentType(TEXT_HTML)), renderer(new ModelTemplateRenderer("Status_html", StatusResource.class).withRenderer(Action.class, actionRenderer())));
    }

    private Renderer<Action> actionRenderer() {
        return new Renderer<Action>() {
            public String render(Action action) throws Exception {
                return String.format("<form action=\"%s\" method=\"POST\"><input type=\"submit\" value=\"%s\"/></form>", action.url(), action.name());
            }
        };
    }
}
