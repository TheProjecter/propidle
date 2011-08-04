package com.googlecode.propidle.scheduling;

import com.googlecode.utterlyidle.modules.Module;

public interface SchedulableRequestModule extends Module {

    public void addTask(SchedulableRequests schedulableRequests);

}
