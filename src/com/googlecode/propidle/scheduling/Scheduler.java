package com.googlecode.propidle.scheduling;

import com.googlecode.propidle.indexing.ConfigurableDelayScheduler;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.utterlyidle.Request;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;


public class Scheduler {

    private final Container applicationContainer;

    private final Map<String, ScheduledFuture<?>> scheduledFutures = new HashMap<String, ScheduledFuture<?>>();

    public Scheduler(Container applicationContainer) {
        this.applicationContainer = applicationContainer;
    }

    public synchronized void schedule(SchedulableTask task) {
        if(scheduledFutures.containsKey(task.name())) {
            scheduledFutures.get(task.name()).cancel(true);
        }

        SimpleContainer container = new SimpleContainer(applicationContainer);
        container.addInstance(PropertyName.class, task.propertyName());
        container.add(ConfigurableDelayScheduler.class);
        scheduledFutures.put(task.name(), container.get(ConfigurableDelayScheduler.class).schedule(task));
    }

}
