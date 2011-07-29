package com.googlecode.propidle.scheduling;

import com.googlecode.utterlyidle.Request;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Scheduler {

    private final Container applicationContainer;
    private final ScheduledExecutorService executorService;

    private final Map<String, ScheduledFuture<?>> scheduledFutures = new HashMap<String, ScheduledFuture<?>>();

    public Scheduler(Container applicationContainer, ScheduledExecutorService executorService) {
        this.applicationContainer = applicationContainer;
        this.executorService = executorService;
    }

    public synchronized void schedule(SchedulableTask task, long initialDelay, long delay, TimeUnit timeUnit) {
        if(scheduledFutures.containsKey(task.name())) {
            scheduledFutures.get(task.name()).cancel(true);
        }

        SimpleContainer container = new SimpleContainer(applicationContainer);
        container.addInstance(Request.class, task.request());
        container.add(RunnableRequest.class);
        ScheduledFuture<?> scheduledFuture = executorService.scheduleWithFixedDelay(container.get(RunnableRequest.class), initialDelay, delay, timeUnit);
        scheduledFutures.put(task.name(), scheduledFuture);
    }

}
