package com.googlecode.propidle.server.sitemesh;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.utterlyidle.MediaType;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.ModuleDefiner;
import com.googlecode.utterlyidle.modules.ModuleDefinitions;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.sitemesh.*;
import com.googlecode.utterlyidle.sitemesh.Decorators;
import com.googlecode.yadic.Container;

import static com.googlecode.propidle.status.StatusResource.NAME;
import static com.googlecode.propidle.urls.MimeType.TEXT_HTML;
import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.URLs.packageUrl;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.sitemesh.StaticDecoratorRule.staticRule;

public class SiteMashDecoratorModule implements RequestScopedModule, ModuleDefiner, SiteMeshModule {
    public Module defineModules(ModuleDefinitions moduleDefinitions) throws Exception {
        moduleDefinitions.addRequestModule(SiteMeshModule.class);
        return this;
    }

    public Module addPerRequestObjects(Container container) throws Exception {
        container.add(Decorators.class);
        container.addInstance(DecoratorProvider.class, provider(container));
        return this;
    }

    public com.googlecode.utterlyidle.sitemesh.Decorators addDecorators(com.googlecode.utterlyidle.sitemesh.Decorators decorators) {
        return decorators.add(staticRule(and(returnsHtml(), not(statusPage())), TemplateName.templateName("decorator")));
    }

    protected DecoratorProvider provider(Container container) {
        return new StringTemplateDecorators(packageUrl(this.getClass()), container);
    }

    public static Predicate<Pair<Request, Response>> statusPage() {
        return new Predicate<Pair<Request, Response>>() {
            public boolean matches(Pair<Request, Response> other) {
                return other.first().uri().path().equals(NAME);
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

}
