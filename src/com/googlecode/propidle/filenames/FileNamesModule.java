package com.googlecode.propidle.filenames;

import com.googlecode.propidle.properties.AllProperties;
import com.googlecode.propidle.search.FileNameSearcher;
import com.googlecode.propidle.search.LuceneFileNameSearcher;
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

public class FileNamesModule implements ResourcesModule, ResponseHandlersModule, RequestScopedModule, ApplicationScopedModule {
    public Module addResources(Resources resources) {
        resources.add(annotatedClass(FileNamesResource.class));
        return this;
    }

    public Module addPerRequestObjects(Container container) {
        container.decorate(AllProperties.class, FileNameIndexingDecorator.class);

        container.add(FileNameSearcher.class, LuceneFileNameSearcher.class);
        return this;
    }

    public Module addResponseHandlers(ResponseHandlers handlers) {
        handlers.add(where(entity(Model.class), nameIs(FileNamesResource.NAME)), renderer(new ModelTemplateRenderer("FileNamesResource_search_html", FileNamesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(FileNamesResource.DIRECTORY_VIEW_NAME)), renderer(new ModelTemplateRenderer("FileNamesResource_directories_html", FileNamesResource.class)));
        return this;
    }

    public Module addPerApplicationObjects(Container container) {
        container.add(FileNameIndex.class, LuceneFileNameIndex.class);
        return this;
    }
}
