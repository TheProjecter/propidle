package com.googlecode.propidle.server.decoration;

import com.googlecode.propidle.urls.MimeType;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.HttpHeaders;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.sitemesh.StringTemplateDecorators;
import com.googlecode.utterlyidle.sitemesh.TemplateName;


import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.utterlyidle.HttpHeaders.*;
import static com.googlecode.utterlyidle.io.Url.url;
import static com.googlecode.utterlyidle.sitemesh.StaticDecoratorRule.staticRule;

public class DecorateHtml extends StringTemplateDecorators {
    public DecorateHtml(BasePath basePath) {
        super(url(DecorateHtml.class.getResource("decorator.st")).parent(), basePath);
        add(staticRule(and(acceptsHtml(), responseIs2xx()), TemplateName.templateName("decorator")));
    }

    private Predicate<? super Pair<Request, Response>> responseIs2xx() {
        return new Predicate<Pair<Request, Response>>() {
            public boolean matches(Pair<Request, Response> requestResponse) {
                return String.valueOf(requestResponse.second().status().code()).startsWith("2");
            }
        };
    }

    private Predicate<? super Pair<Request, Response>> acceptsHtml() {
        return new Predicate<Pair<Request, Response>>() {
            public boolean matches(Pair<Request, Response> requestResponse) {
                String contentTypeHeader = requestResponse.second().headers().getValue(CONTENT_TYPE);
                return contentTypeHeader != null && contentTypeHeader.contains(MimeType.TEXT_HTML.value());
            }
        };
    }
}
