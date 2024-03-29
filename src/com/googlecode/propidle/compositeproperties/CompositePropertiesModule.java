package com.googlecode.propidle.compositeproperties;

import com.googlecode.propidle.server.ModelTemplateRenderer;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.utterlyidle.modules.ResponseHandlersModule;
import com.googlecode.utterlyidle.rendering.Model;

import static com.googlecode.propidle.ModelName.nameIs;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;

public class CompositePropertiesModule implements ResourcesModule, ResponseHandlersModule {
    public Resources addResources(Resources resources) {
        return resources.add(annotatedClass(CompositePropertiesResource.class));
    }

    public ResponseHandlers addResponseHandlers(ResponseHandlers handlers) {
        return handlers.add(where(entity(Model.class), nameIs(CompositePropertiesResource.NAME)), renderer(new ModelTemplateRenderer("CompositePropertiesResource_html", CompositePropertiesResource.class)));
    }
}
