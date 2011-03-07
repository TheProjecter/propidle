package com.googlecode.propidle.status;

import com.googlecode.propidle.server.ModelTemplateRenderer;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.utterlyidle.modules.ResponseHandlersModule;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.yadic.Container;

import static com.googlecode.propidle.server.PropertiesModule.nameIs;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;

public class StatusModule implements RequestScopedModule, ResourcesModule, ResponseHandlersModule {

    public Module addPerRequestObjects(final Container container) {
        container.add(StatusChecks.class);
        container.get(StatusChecks.class).add(LuceneDirectoryCheck.class);
        return this;
    }

    public Module addResources(Resources resources) {
        resources.add(StatusResource.class);
        return this;
    }

    public Module addResponseHandlers(ResponseHandlers handlers) {
        handlers.add(where(entity(Model.class), nameIs(StatusResource.NAME)), renderer(new ModelTemplateRenderer("Status_html", StatusResource.class)));
        return this;
    }
}
