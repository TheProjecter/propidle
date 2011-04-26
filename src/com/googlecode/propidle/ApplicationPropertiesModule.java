package com.googlecode.propidle;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.propidle.client.PlainTextDynamicPropertiesActivator;
import com.googlecode.propidle.client.SnapshotPropertiesActivator;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

import java.util.Properties;
import java.util.concurrent.Callable;

public class ApplicationPropertiesModule implements ApplicationScopedModule, RequestScopedModule {
    private final Callable<Properties> propertyLoader;

    public ApplicationPropertiesModule(Callable<Properties> propertyLoader) {
        this.propertyLoader = propertyLoader;
    }

    public Module addPerApplicationObjects(Container container) {
        container.addActivator(DynamicProperties.class, new PlainTextDynamicPropertiesActivator(propertyLoader));
        container.addActivator(Properties.class, SnapshotPropertiesActivator.class);
        return this;
    }

    public Module addPerRequestObjects(Container container) {
        container.addActivator(Properties.class, SnapshotPropertiesActivator.class);
        return this;
    }
}
