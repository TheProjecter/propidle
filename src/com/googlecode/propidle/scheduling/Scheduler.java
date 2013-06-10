package com.googlecode.propidle.scheduling;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Scheduler implements Closeable {
    private final ScheduledExecutorService executorService;
    private final Map<Runnable, ScheduledFuture<?>> scheduledFutures = new HashMap<Runnable, ScheduledFuture<?>>();

    public Scheduler(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    public synchronized void schedule(Runnable task, long initialDelay, long delay, TimeUnit timeUnit) {
        if(scheduledFutures.containsKey(task)) {
            scheduledFutures.get(task).cancel(true);
        }

        ScheduledFuture<?> scheduledFuture = executorService.scheduleWithFixedDelay(task, initialDelay, delay, timeUnit);
        scheduledFutures.put(task, scheduledFuture);
    }

    @Override
    public void close() throws IOException {
        executorService.shutdownNow();
    }
}
