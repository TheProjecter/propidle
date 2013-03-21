package com.googlecode.propidle.migrations;

import com.googlecode.propidle.server.ModelTemplateRenderer;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.propidle.migrations.ModuleMigrationsCollector;
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

public class PropidleMigrationsModule implements RequestScopedModule, ResourcesModule, ResponseHandlersModule {
    public Container addPerRequestObjects(Container container) {
        container.get(ModuleMigrationsCollector.class).add(PropIdleMigrations.class);
        return container;
    }

    public Resources addResources(Resources resources) {
        return resources.add(annotatedClass(MigrationResource.class));
    }

    public ResponseHandlers addResponseHandlers(ResponseHandlers handlers) {
        return handlers.add(where(entity(Model.class), nameIs(MigrationResource.NAME)), renderer(new ModelTemplateRenderer("MigrationResource_html", MigrationResource.class)));
    }
}
