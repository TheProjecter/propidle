package com.googlecode.propidle;

import com.googlecode.propidle.properties.AllProperties;
import com.googlecode.propidle.properties.AllPropertiesFromChanges;
import com.googlecode.propidle.properties.PropertyDiffTool;
import com.googlecode.propidle.properties.UtterlyIdleUrlResolver;
import com.googlecode.propidle.server.ConvertRevisionNumberQueryParameterToHeader;
import com.googlecode.propidle.server.RequestedRevisionNumber;
import com.googlecode.propidle.server.RequestedRevisionNumberActivator;
import com.googlecode.propidle.server.TransactionDecorator;
import com.googlecode.propidle.server.decoration.DecorateHtml;
import com.googlecode.propidle.urls.RelativeUriGetter;
import com.googlecode.propidle.urls.SimpleUriGetter;
import com.googlecode.propidle.urls.UriGetter;
import com.googlecode.propidle.urls.UrlResolver;
import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.util.time.SystemClock;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.handlers.ConvertExtensionToAcceptHeader;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.sitemesh.Decorators;
import com.googlecode.utterlyidle.sitemesh.SiteMeshHandler;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.generics.TypeFor;
import com.googlecode.yadic.resolvers.OptionResolver;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.utterlyidle.handlers.ConvertExtensionToAcceptHeader.Replacements.replacements;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

public class BasicModule implements RequestScopedModule {
    public Module addPerRequestObjects(Container container) {
        // HttpHandler decoration
        container.addInstance(ConvertExtensionToAcceptHeader.Replacements.class,
                replacements(pair("properties", TEXT_PLAIN), pair("html", TEXT_HTML)));
        container.decorate(HttpHandler.class, ConvertExtensionToAcceptHeader.class);

        container.decorate(HttpHandler.class, ConvertRevisionNumberQueryParameterToHeader.class);
        container.addActivator(RequestedRevisionNumber.class, RequestedRevisionNumberActivator.class);
        container.add(new TypeFor<Option<RequestedRevisionNumber>>(){{}}.get(), new OptionResolver(container, instanceOf(RequestedRevisionNumberActivator.class)));

        container.decorate(HttpHandler.class, TransactionDecorator.class);

        container.add(Decorators.class, DecorateHtml.class);
        container.decorate(HttpHandler.class, SiteMeshHandler.class);

        // Shared tools
        container.add(Clock.class, SystemClock.class);
        container.add(PropertyDiffTool.class, PropertyDiffTool.class);

        container.add(UrlResolver.class, UtterlyIdleUrlResolver.class);
        container.add(UriGetter.class, SimpleUriGetter.class);
        container.decorate(UriGetter.class, RelativeUriGetter.class);

        // Shared repositories
        container.add(AllProperties.class, AllPropertiesFromChanges.class);
        return this;
    }
}
