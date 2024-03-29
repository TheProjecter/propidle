package com.googlecode.propidle.client;

import com.googlecode.propidle.PropertyTriggeredExecutor;
import com.googlecode.propidle.client.handlers.IgnoreAttemptsFailureHandler;
import com.googlecode.propidle.client.handlers.LoggingFailureHandler;
import com.googlecode.propidle.client.logging.Logger;
import com.googlecode.propidle.client.logging.TimestampLogger;
import com.googlecode.propidle.properties.PropertyValue;
import com.googlecode.propidle.scheduling.Scheduler;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.time.Clock;
import com.googlecode.totallylazy.time.SystemClock;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

import static com.googlecode.propidle.client.logging.PrintStreamLogger.printStreamLogger;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static java.lang.Long.valueOf;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;

public class DynamicPropertiesReloadingScheduler implements Closeable {
    private final PropertyTriggeredExecutor propertyTriggeredExecutor;
    private final Scheduler scheduler;
    private final ReloadPropertiesRunnable reloadPropertiesRunnable;

    public static DynamicPropertiesReloadingScheduler dynamicPropertiesReloadingScheduler(DynamicProperties dynamicProperties) {
        return dynamicPropertiesReloadingScheduler(dynamicProperties, newSingleThreadScheduledExecutor(), new SystemClock());
    }

    public static DynamicPropertiesReloadingScheduler dynamicPropertiesReloadingScheduler(DynamicProperties dynamicProperties, ScheduledExecutorService executorService) {
        return dynamicPropertiesReloadingScheduler(dynamicProperties, executorService, TimestampLogger.timestampLogger(printStreamLogger(System.err), new SystemClock()));
    }

    public static DynamicPropertiesReloadingScheduler dynamicPropertiesReloadingScheduler(DynamicProperties dynamicProperties, ScheduledExecutorService executorService, Logger logger) {
        Integer attempts = Integer.valueOf(dynamicProperties.snapshot().getProperty("propidle.connection.attempts", "3"));
        IgnoreAttemptsFailureHandler failureHandler = new IgnoreAttemptsFailureHandler(attempts, new LoggingFailureHandler(logger));
        ReloadPropertiesRunnable reloadPropertiesRunnable = new ReloadPropertiesRunnable(dynamicProperties, failureHandler);
        return new DynamicPropertiesReloadingScheduler(new Scheduler(executorService), new PropertyTriggeredExecutor(dynamicProperties), reloadPropertiesRunnable).registerReload();
    }

    private static DynamicPropertiesReloadingScheduler dynamicPropertiesReloadingScheduler(DynamicProperties dynamicProperties, ScheduledExecutorService executorService, Clock clock) {
        return dynamicPropertiesReloadingScheduler(dynamicProperties, executorService, TimestampLogger.timestampLogger(printStreamLogger(System.err), clock));
    }

    public DynamicPropertiesReloadingScheduler(Scheduler scheduler, final PropertyTriggeredExecutor propertyTriggeredExecutor, final ReloadPropertiesRunnable reloadPropertiesRunnable) {
        this.scheduler = scheduler;
        this.propertyTriggeredExecutor = propertyTriggeredExecutor;
        this.reloadPropertiesRunnable = reloadPropertiesRunnable;
    }

    public DynamicPropertiesReloadingScheduler registerReload() {
        propertyTriggeredExecutor.register(propertyName("property.reload.time.in.seconds"), scheduleReloading(), propertyValue("60"));
        return this;
    }

    private Callable1<PropertyValue, Void> scheduleReloading() {
        return new Callable1<PropertyValue, Void>() {
            public Void call(PropertyValue propertyValue) throws Exception {
                long delay = valueOf(propertyValue.value());
                scheduler.schedule(reloadPropertiesRunnable, delay, delay, SECONDS);
                return null;
            }
        };
    }

    @Override
    public void close() throws IOException {
        scheduler.close();
    }
}
