package com.googlecode.propidle;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.propidle.client.DynamicPropertiesActivator;
import com.googlecode.propidle.client.SnapshotPropertiesActivator;
import com.googlecode.propidle.properties.ReloadPropertiesResource;
import com.googlecode.propidle.scheduling.SchedulableRequestModule;
import com.googlecode.propidle.scheduling.SchedulableRequests;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.yadic.Container;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;

public class ApplicationPropertiesModule implements ApplicationScopedModule, RequestScopedModule, SchedulableRequestModule, ResourcesModule {
    public static final String RELOAD_PROPERTIES_TASK_NAME = "reloadProperties";
    private final Callable<Properties> propertyLoader;

    public ApplicationPropertiesModule(Callable<Properties> propertyLoader) {
        this.propertyLoader = propertyLoader;
    }

    public Container addPerApplicationObjects(Container container) {
        container.addActivator(DynamicProperties.class, new DynamicPropertiesActivator(propertyLoader));
        return container.addActivator(Properties.class, SnapshotPropertiesActivator.class);
    }

    public Container addPerRequestObjects(Container container) {
        return container.addActivator(Properties.class, SnapshotPropertiesActivator.class);
    }

    public void addTask(SchedulableRequests schedulableRequests) {
        schedulableRequests.addTask(RELOAD_PROPERTIES_TASK_NAME, post(ReloadPropertiesResource.NAME).build());

    }

    public Resources addResources(Resources resources) {
        return resources.add(annotatedClass(ReloadPropertiesResource.class));
    }
}
