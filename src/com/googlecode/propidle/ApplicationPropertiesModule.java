package com.googlecode.propidle;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.propidle.client.DynamicPropertiesActivator;
import com.googlecode.propidle.client.SnapshotPropertiesActivator;
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
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.sitemesh.Decorators;
import com.googlecode.utterlyidle.sitemesh.SiteMeshHandler;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.generics.TypeFor;
import com.googlecode.yadic.resolvers.OptionResolver;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.utterlyidle.handlers.ConvertExtensionToAcceptHeader.Replacements.replacements;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

public class ApplicationPropertiesModule implements ApplicationScopedModule, RequestScopedModule {
    private final Callable<Properties> propertyLoader;

    public ApplicationPropertiesModule(Callable<Properties> propertyLoader) {
        this.propertyLoader = propertyLoader;
    }

    public Module addPerApplicationObjects(Container container) {
        container.addActivator(DynamicProperties.class, new DynamicPropertiesActivator(propertyLoader));
        container.addActivator(Properties.class, SnapshotPropertiesActivator.class);
        return this;
    }

    public Module addPerRequestObjects(Container container) {
        container.addActivator(Properties.class, SnapshotPropertiesActivator.class);
        return this;
    }
}
