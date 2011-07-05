package com.googlecode.propidle.search;

import com.googlecode.propidle.properties.AllProperties;
import com.googlecode.propidle.server.ModelTemplateRenderer;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.AbstractModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.utterlyidle.modules.ResponseHandlersModule;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.yadic.Container;

import static com.googlecode.propidle.ModelName.nameIs;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;

public class SearchModule extends AbstractModule{
    public Module addPerRequestObjects(Container container){
        container.add(PropertiesIndex.class, LucenePropertiesIndex.class);
        container.add(PropertiesSearcher.class, LucenePropertiesSearcher.class);

        container.decorate(AllProperties.class, PropertiesIndexingDecorator.class);
        return this;
    }
    public Module addResources(Resources resources) {
        resources.add(annotatedClass(SearchResource.class));
        return null;
    }

    public Module addResponseHandlers(ResponseHandlers handlers) {
        handlers.add(where(entity(Model.class), nameIs(SearchResource.NAME)), renderer(new ModelTemplateRenderer("SearchResource_html", SearchResource.class)));
        return this;
    }
}
