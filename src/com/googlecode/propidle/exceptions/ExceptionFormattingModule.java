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

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.utterlyidle.HeaderParameters.headerParameters;
import static com.googlecode.utterlyidle.HttpHeaders.ACCEPT;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.Status.INTERNAL_SERVER_ERROR;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;
import static com.googlecode.utterlyidle.rendering.Model.model;


public class ExceptionFormattingModule implements RequestScopedModule, ResponseHandlersModule {
    public Module addPerRequestObjects(Container container) throws Exception {
        container.decorate(HttpHandler.class, ExceptionHandler.class);
        return this;
    }

    public Module addResponseHandlers(ResponseHandlers handlers) throws Exception {
        LogicalPredicate<Pair<Request,Response>> where = Predicates.<Pair<Request, Response>, Object>where(entity(), is(instanceOf(Exception.class))).and(acceptsHtml());
        handlers.add(where, exceptionResponseHandler());
        return this;
    }

    private ResponseHandler exceptionResponseHandler() {
        return new ResponseHandler() {
            public Response handle(Response response) throws Exception {

                Response htmlResponse = Responses.response(INTERNAL_SERVER_ERROR).
                        header(CONTENT_TYPE, TEXT_HTML).
                        entity(model().
                                add("errorMessage", "An error occured in propidle").
                                add("stackTrace", ExceptionRenderer.toString((Exception) response.entity())));
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
