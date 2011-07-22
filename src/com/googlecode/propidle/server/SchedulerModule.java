package com.googlecode.propidle.server;

import com.googlecode.utterlyidle.modules.Module;

public interface SchedulerModule extends Module {
    public void addTo(Scheduler scheduler);
}
