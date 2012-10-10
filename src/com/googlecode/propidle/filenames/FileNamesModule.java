package com.googlecode.propidle.filenames;

import com.googlecode.propidle.properties.AllProperties;
import com.googlecode.propidle.search.FileNameSearcher;
import com.googlecode.propidle.search.LuceneFileNameSearcher;
import com.googlecode.propidle.server.ModelTemplateRenderer;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.utterlyidle.Renderer;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.*;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.yadic.Container;

import static com.googlecode.propidle.ModelName.nameIs;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;

public class FileNamesModule implements ResourcesModule, ResponseHandlersModule, RequestScopedModule {
    public Module addResources(Resources resources) {
        resources.add(annotatedClass(FileNamesResource.class));
        return this;
    }

    public Module addPerRequestObjects(Container container) {
        container.add(FileNameIndex.class, LuceneFileNameIndex.class);
        container.add(FileNameSearcher.class, LuceneFileNameSearcher.class);
        container.decorate(AllProperties.class, FileNameIndexingDecorator.class);
        return this;
    }

    public Module addResponseHandlers(ResponseHandlers handlers) {
        handlers.add(and(where(entity(Model.class), nameIs(FileNamesResource.NAME)),not(requestAccepts(TEXT_PLAIN))), renderer(new ModelTemplateRenderer("FileNamesResource_search_html", FileNamesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(FileNamesResource.DIRECTORY_VIEW_NAME)), renderer(new ModelTemplateRenderer("FileNamesResource_directories_html", FileNamesResource.class)));
        handlers.add(and(where(entity(Model.class), nameIs(FileNamesResource.NAME)),requestAccepts(TEXT_PLAIN)), renderer(new ModelAutoCompleteRenderer()));
        return this;
    }

    private LogicalPredicate<Pair<Request, Response>> requestAccepts(final String mediaType) {
        return new LogicalPredicate<Pair<Request, Response>>() {
            public boolean matches(Pair<Request, Response> requestResponsePair) {
                return requestResponsePair.first().headers().getValue("Accept")!=null &&
                        requestResponsePair.first().headers().getValue("Accept").equals(mediaType);
            }
        };
    }

    private class ModelAutoCompleteRenderer implements Renderer<Model> {
        private final int MAX_NUMBER_OF_RESULTS=20;

        public String render(Model model) throws Exception {
            Sequence<Model> fileNames = sequence(model.get("filenames")).safeCast(Model.class);
            if(fileNames.isEmpty()){
                return "";
            } else {
                return  fileNames.map(toUrl()).take(MAX_NUMBER_OF_RESULTS).toString(",");
            }
        }

        private Callable1<Model, String> toUrl() {
            return new Callable1<Model,String>() {
                public String call(Model filename) throws Exception {
                    return filename.first("url").toString();
                }
            };
        }

    }
}
