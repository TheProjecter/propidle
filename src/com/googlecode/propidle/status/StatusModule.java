package com.googlecode.propidle.status;

import com.googlecode.propidle.PersistenceMechanism;
import com.googlecode.propidle.server.ModelTemplateRenderer;
import com.googlecode.utterlyidle.Renderer;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.utterlyidle.modules.ResponseHandlersModule;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.yadic.Container;

import java.util.Properties;

import static com.googlecode.propidle.ModelName.nameIs;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;

public class StatusModule implements RequestScopedModule, ResourcesModule, ResponseHandlersModule {

    public Module addPerRequestObjects(final Container container) {
        container.add(StatusChecks.class);
        container.get(StatusChecks.class).add(DisplayBuildNumber.class);
        container.get(StatusChecks.class).add(LuceneDirectoryCheck.class);
        Properties properties = container.get(Properties.class);
        PersistenceMechanism mechanism = PersistenceMechanism.fromProperties(properties);
        if (mechanism != PersistenceMechanism.IN_MEMORY) {
            container.get(StatusChecks.class).add(ConnectionDetailsCheck.class);
            container.get(StatusChecks.class).add(DatabaseVersionCheck.class);
        }
        return this;
    }

    public Module addResources(Resources resources) {
        resources.add(StatusResource.class);
        return this;
    }

    public Module addResponseHandlers(ResponseHandlers handlers) {
        handlers.add(where(entity(Model.class), nameIs(StatusResource.NAME)), renderer(new ModelTemplateRenderer("Status_html", StatusResource.class).withRenderer(Action.class, actionRenderer())));
        return this;
    }

    private Renderer<Action> actionRenderer() {
        return new Renderer<Action>() {
            public String render(Action action) throws Exception {
                return String.format("<form action=\"%s\" method=\"POST\"><input type=\"submit\" value=\"%s\"/></form>", action.url(), action.name());
            }
        };
    }
}
