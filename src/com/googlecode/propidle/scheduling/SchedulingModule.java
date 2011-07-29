package com.googlecode.propidle.scheduling;

import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.modules.*;
import com.googlecode.yadic.Container;

import java.util.concurrent.ScheduledExecutorService;

import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static java.util.concurrent.Executors.newScheduledThreadPool;

public class SchedulingModule implements ResourcesModule, ModuleDefiner, ApplicationScopedModule {
    public Module addResources(Resources resources) {
        resources.add(annotatedClass(ScheduleResource.class));
        return this;
    }

    public Module defineModules(ModuleDefinitions moduleDefinitions) {
        moduleDefinitions.addApplicationModule(SchedulableTaskModule.class);
        return this;
    }

    public Module addPerApplicationObjects(Container container) {
        container.add(ScheduleTaskRequest.class);
        container.addInstance(ScheduledExecutorService.class, newScheduledThreadPool(5));
        container.add(SchedulableTasks.class);
        container.add(Scheduler.class);
        return this;
    }
}
