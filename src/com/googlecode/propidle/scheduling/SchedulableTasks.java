package com.googlecode.propidle.scheduling;

import com.googlecode.propidle.scheduling.SchedulableTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SchedulableTasks {

    private final Map<String, SchedulableTask> tasks = new HashMap<String, SchedulableTask>();

    public void addTask(SchedulableTask task) {
        tasks.put(task.name(), task);
    }

    public Collection availableTaskNames() {
        return tasks.keySet();
    }

    public SchedulableTask getTask(String name) {
        return tasks.get(name);
    }
}


