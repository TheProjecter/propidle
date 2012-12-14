package com.googlecode.propidle.exceptions;

import com.googlecode.propidle.server.ModelTemplateRenderer;
import com.googlecode.propidle.urls.MimeType;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.handlers.ExceptionHandler;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ResponseHandlersModule;
import com.googlecode.utterlyidle.rendering.ExceptionRenderer;
import com.googlecode.yadic.Container;

import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.utterlyidle.HttpHeaders.ACCEPT;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.Status.INTERNAL_SERVER_ERROR;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;
import static com.googlecode.utterlyidle.rendering.Model.model;


public class ExceptionFormattingModule implements RequestScopedModule, ResponseHandlersModule {
    public Container addPerRequestObjects(Container container) throws Exception {
        return container.decorate(HttpHandler.class, ExceptionHandler.class);
    }

    public ResponseHandlers addResponseHandlers(ResponseHandlers handlers) throws Exception {
        LogicalPredicate<Pair<Request,Response>> where = Predicates.<Pair<Request, Response>, Object>where(entity(), is(instanceOf(Exception.class))).and(acceptsHtml());
        return handlers.add(where, exceptionResponseHandler());
    }

    private ResponseHandler exceptionResponseHandler() {
        return new ResponseHandler() {
            public Response handle(Response response) throws Exception {

                Response htmlResponse = ResponseBuilder.response(INTERNAL_SERVER_ERROR).
                        header(CONTENT_TYPE, TEXT_HTML).
                        entity(model().
                                add("errorMessage", "An error occured in propidle").
                                add("stackTrace", ExceptionRenderer.toString((Exception) response.entity().value()))).build();
                return renderer(new ModelTemplateRenderer("Error_html", ExceptionFormattingModule.class)).handle(htmlResponse);
            }
        };
    }

    public static Predicate<? super Pair<Request, Response>> acceptsHtml() {
        return new Predicate<Pair<Request, Response>>() {
            public boolean matches(Pair<Request, Response> requestResponsePair) {
                String acceptsString = requestResponsePair.first().headers().getValue(ACCEPT);
                return acceptsString != null && (acceptsString.contains(MediaType.WILDCARD) || acceptsString.contains(MimeType.TEXT_HTML.value()));
            }
        };
    }

}
