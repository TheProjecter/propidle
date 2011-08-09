package com.googlecode.propidle.server.decoration;

import com.googlecode.propidle.urls.MimeType;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.sitemesh.ActivateSiteMeshModule;
import com.googlecode.utterlyidle.sitemesh.TemplateName;

import static com.googlecode.propidle.status.StatusResource.NAME;
import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.URLs.packageUrl;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.sitemesh.StaticDecoratorRule.staticRule;
import static com.googlecode.utterlyidle.sitemesh.StringTemplateDecorators.stringTemplateDecorators;

public class Decorators {

    public static ActivateSiteMeshModule decoratorsModule() {
        return stringTemplateDecorators(packageUrl(Decorators.class),
                staticRule(and(acceptsHtml(), responseIs2xx(), not(statusPage())), TemplateName.templateName("decorator")));
    }

    private static Predicate<Pair<Request, Response>> statusPage() {
        return new Predicate<Pair<Request, Response>>() {
            public boolean matches(Pair<Request, Response> other) {
                return other.first().url().path().equals(NAME);
            }
        };
    }

    private static Predicate<? super Pair<Request, Response>> responseIs2xx() {
        return new Predicate<Pair<Request, Response>>() {
            public boolean matches(Pair<Request, Response> requestResponse) {
                return String.valueOf(requestResponse.second().status().code()).startsWith("2");
            }
        };
    }

    private static Predicate<? super Pair<Request, Response>> acceptsHtml() {
        return new Predicate<Pair<Request, Response>>() {
            public boolean matches(Pair<Request, Response> requestResponse) {
                String contentTypeHeader = requestResponse.second().headers().getValue(CONTENT_TYPE);
                return contentTypeHeader != null && contentTypeHeader.contains(MimeType.TEXT_HTML.value());
            }
        };
    }
}
