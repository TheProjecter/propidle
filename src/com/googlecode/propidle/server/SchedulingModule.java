package com.googlecode.propidle.server;

import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.ModuleDefiner;
import com.googlecode.utterlyidle.modules.ModuleDefinitions;

public class SchedulingModule implements ModuleDefiner {
    public Module defineModules(ModuleDefinitions moduleDefinitions) {
        moduleDefinitions.addApplicationModule(SchedulerModule.class);
        return this;
    }
}
