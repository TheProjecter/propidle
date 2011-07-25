package com.googlecode.propidle.scheduling;

import com.googlecode.propidle.scheduling.SchedulableTasks;
import com.googlecode.utterlyidle.modules.Module;

public interface SchedulableTaskModule extends Module {

    public void addTask(SchedulableTasks tasks);

}
