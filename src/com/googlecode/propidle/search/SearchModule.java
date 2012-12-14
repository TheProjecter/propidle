package com.googlecode.propidle.search;

import com.googlecode.propidle.properties.AllProperties;
import com.googlecode.propidle.server.ModelTemplateRenderer;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.*;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.yadic.Container;

import static com.googlecode.propidle.ModelName.nameIs;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;

public class SearchModule implements ApplicationScopedModule, RequestScopedModule, ResourcesModule, ResponseHandlersModule{
    @Override
    public Container addPerApplicationObjects(Container container) {
        return container.add(PropertiesSearcher.class, LucenePropertiesSearcher.class);
    }

    public Container addPerRequestObjects(Container container){
        container.add(PropertiesIndex.class, LucenePropertiesIndex.class);
        return container.decorate(AllProperties.class, PropertiesIndexingDecorator.class);
    }
    public Resources addResources(Resources resources) {
        return resources.add(annotatedClass(SearchResource.class));
    }

    public ResponseHandlers addResponseHandlers(ResponseHandlers handlers) {
        return handlers.add(where(entity(Model.class), nameIs(SearchResource.NAME)), renderer(new ModelTemplateRenderer("SearchResource_html", SearchResource.class)));
    }
}
