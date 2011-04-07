package com.googlecode.propidle.aliases;

import com.googlecode.propidle.server.ModelTemplateRenderer;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.utterlyidle.modules.ResponseHandlersModule;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.yadic.Container;

import static com.googlecode.propidle.ModelName.nameIs;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;

public class AliasesModule implements RequestScopedModule, ResourcesModule, ResponseHandlersModule {
    public Module addPerRequestObjects(Container container) {
        container.add(Aliases.class, AliasesFromRecords.class);
        return this;
    }

    public Module addResources(Resources resources) {
        resources.add(AliasesResource.class);
        return this;
    }

    public Module addResponseHandlers(ResponseHandlers handlers) {
        handlers.add(where(entity(Model.class), nameIs(AliasesResource.ALIAS)), renderer(new ModelTemplateRenderer("AliasResource_html", AliasesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(AliasesResource.ALL_ALIASES)), renderer(new ModelTemplateRenderer("AliasesResource_html", AliasesResource.class)));
        return this;
    }
}
