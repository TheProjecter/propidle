package com.googlecode.propidle.server.decoration;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.utterlyidle.MediaType;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.sitemesh.ActivateSiteMeshModule;
import com.googlecode.utterlyidle.sitemesh.TemplateName;

import static com.googlecode.propidle.status.StatusResource.NAME;
import static com.googlecode.propidle.urls.MimeType.TEXT_HTML;
import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.URLs.packageUrl;
import static com.googlecode.utterlyidle.HttpHeaders.ACCEPT;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.sitemesh.StaticDecoratorRule.staticRule;
import static com.googlecode.utterlyidle.sitemesh.StringTemplateDecorators.stringTemplateDecorators;

public class Decorators {

    public static ActivateSiteMeshModule decoratorsModule() {
        return stringTemplateDecorators(packageUrl(Decorators.class),
                staticRule(and(returnsHtml(), not(statusPage())), TemplateName.templateName("decorator")));
    }

    private static Predicate<Pair<Request, Response>> statusPage() {
        return new Predicate<Pair<Request, Response>>() {
            public boolean matches(Pair<Request, Response> other) {
                return other.first().url().path().equals(NAME);
            }
        };
    }

    public static Predicate<? super Pair<Request, Response>> returnsHtml() {
        return new Predicate<Pair<Request, Response>>() {
            public boolean matches(Pair<Request, Response> requestResponse) {
                String contentTypeHeader = requestResponse.second().headers().getValue(CONTENT_TYPE);
                return contentTypeHeader != null && contentTypeHeader.contains(TEXT_HTML.value());
            }
        };
    }

    public static Predicate<? super Pair<Request, Response>> acceptsHtml() {
        return new Predicate<Pair<Request, Response>>() {
            public boolean matches(Pair<Request, Response> requestResponsePair) {
                String acceptsString = requestResponsePair.first().headers().getValue(ACCEPT);
                return acceptsString.contains(MediaType.WILDCARD) || acceptsString.contains(TEXT_HTML.value());
            }
        };
    }
}
