package com.googlecode.propidle.navigation;

import com.googlecode.propidle.aliases.AliasesResource;
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

public class NavigationModule implements ResourcesModule, ResponseHandlersModule {
    public Resources addResources(Resources bindings) throws Exception {
        return bindings.add(annotatedClass(NavigationResource.class));
    }

    public ResponseHandlers addResponseHandlers(ResponseHandlers responseHandlers) throws Exception {
        return responseHandlers.add(where(entity(Model.class), nameIs(NavigationResource.PATH)), renderer(new ModelTemplateRenderer("NavigationResource_html", NavigationResource.class)));
    }
}
