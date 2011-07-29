package com.googlecode.propidle;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.propidle.client.DynamicPropertiesActivator;
import com.googlecode.propidle.client.SnapshotPropertiesActivator;
import com.googlecode.propidle.properties.ReloadPropertiesResource;
import com.googlecode.propidle.scheduling.SchedulableTask;
import com.googlecode.propidle.scheduling.SchedulableTaskModule;
import com.googlecode.propidle.scheduling.SchedulableTasks;
import com.googlecode.utterlyidle.RequestBuilder;
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

public class ApplicationPropertiesModule implements ApplicationScopedModule, RequestScopedModule, SchedulableTaskModule, ResourcesModule {
    public static final String RELOAD_PROPERTIES_TASK_NAME = "reloadProperties";
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

    public void addTask(SchedulableTasks tasks) {
        tasks.addTask(new SchedulableTask(RELOAD_PROPERTIES_TASK_NAME, post(ReloadPropertiesResource.NAME).build()));

    }

    public Module addResources(Resources resources) {
        resources.add(annotatedClass(ReloadPropertiesResource.class));
        return this;
    }
}
