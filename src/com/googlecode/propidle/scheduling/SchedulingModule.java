package com.googlecode.propidle.scheduling;

import com.googlecode.utterlyidle.CloseableCallable;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.modules.*;
import com.googlecode.yadic.Container;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static java.util.concurrent.Executors.newScheduledThreadPool;

public class SchedulingModule implements ResourcesModule, ModuleDefiner, ApplicationScopedModule {
    public Module addResources(Resources resources) {
        resources.add(annotatedClass(ScheduleResource.class));
        return this;
    }

    public Module defineModules(ModuleDefinitions moduleDefinitions) {
        moduleDefinitions.addApplicationModule(SchedulableRequestModule.class);
        return this;
    }

    public Module addPerApplicationObjects(Container container) {
        container.add(ScheduleTask.class, ScheduleTaskRequest.class);
        container.addActivator(ScheduledExecutorService.class, closeableExecutorService());
        container.add(SchedulableRequests.class);
        container.add(Scheduler.class);
        return this;
    }

    private CloseableCallable<ScheduledExecutorService> closeableExecutorService() {
        return new CloseableCallable<ScheduledExecutorService>() {
            private final ScheduledExecutorService scheduledExecutorService = newScheduledThreadPool(5);

            @Override
            public ScheduledExecutorService call() throws Exception {
                return scheduledExecutorService;
            }

            @Override
            public void close() throws IOException {
                scheduledExecutorService.shutdownNow();
            }
        };
    }
}
