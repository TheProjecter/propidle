package com.googlecode.propidle.versioncontrol.changes;

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
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;

public class ChangesModule implements RequestScopedModule, ResourcesModule, ResponseHandlersModule {
    public Module addPerRequestObjects(Container container) {
        container.add(AllChanges.class, AllChangesFromRecords.class);
        return this;
    }

    public Module addResources(Resources resources) {
        resources.add(annotatedClass(ChangesResource.class));
        return this;
    }

    public Module addResponseHandlers(ResponseHandlers handlers) {
        handlers.add(where(entity(Model.class), nameIs(ChangesResource.NAME)), renderer(new ModelTemplateRenderer("ChangesResource_html", ChangesResource.class)));
        return this;
    }
}
