package com.googlecode.propidle.scheduling;

import com.googlecode.propidle.indexing.ConfigurableDelayScheduler;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.utterlyidle.Request;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;

import java.util.HashSet;
import java.util.Set;


public class Scheduler {

    private final Container applicationContainer;
    private final Set<String> scheduledTaskNames = new HashSet<String>();

    public Scheduler(Container applicationContainer) {
        this.applicationContainer = applicationContainer;
    }

    public synchronized boolean schedule(SchedulableTask task) {
        if (scheduledTaskNames.contains(task.name())) {
            return false;
        } else {
            SimpleContainer container = new SimpleContainer(applicationContainer);

            container.addInstance(PropertyName.class, task.propertyName());
            container.add(ConfigurableDelayScheduler.class);
            container.get(ConfigurableDelayScheduler.class).schedule(task);
            scheduledTaskNames.add(task.name());
            return true;
        }
    }

}
