package com.googlecode.propidle.scheduling;

import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.ModuleDefiner;
import com.googlecode.utterlyidle.modules.ModuleDefinitions;
import com.googlecode.utterlyidle.modules.ResourcesModule;

import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;

public class SchedulingModule implements ResourcesModule, ModuleDefiner {
    public Module addResources(Resources resources) {
        resources.add(annotatedClass(ScheduleResource.class));
        return this;
    }

    public Module defineModules(ModuleDefinitions moduleDefinitions) {
        moduleDefinitions.addApplicationModule(SchedulableTaskModule.class);
        return this;
    }
}
