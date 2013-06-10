package com.googlecode.propidle.client;

import com.googlecode.propidle.PropertyTriggeredExecutor;
import com.googlecode.propidle.client.logging.Logger;
import com.googlecode.propidle.client.logging.PrintStreamLogger;
import com.googlecode.propidle.properties.PropertyValue;
import com.googlecode.propidle.scheduling.Scheduler;
import com.googlecode.totallylazy.Callable1;

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

    public static DynamicProperties reloadingDynamicProperties(DynamicProperties dynamicProperties) {
        return reloadingDynamicProperties(dynamicProperties, newSingleThreadScheduledExecutor());
    }

    public static DynamicProperties reloadingDynamicProperties(DynamicProperties dynamicProperties, ScheduledExecutorService executorService, Logger logger) {
        new DynamicPropertiesReloadingScheduler(new Scheduler(executorService), new PropertyTriggeredExecutor(dynamicProperties), new ReloadPropertiesRunnable(dynamicProperties, logger)).registerReload();
        return dynamicProperties;
    }

    public static DynamicProperties reloadingDynamicProperties(DynamicProperties dynamicProperties, ScheduledExecutorService executorService) {
        return reloadingDynamicProperties(dynamicProperties, executorService, printStreamLogger(System.err));
    }

    public DynamicPropertiesReloadingScheduler(Scheduler scheduler, final PropertyTriggeredExecutor propertyTriggeredExecutor, final ReloadPropertiesRunnable reloadPropertiesRunnable) {
        this.scheduler = scheduler;
        this.propertyTriggeredExecutor = propertyTriggeredExecutor;
        this.reloadPropertiesRunnable = reloadPropertiesRunnable;
    }

    public void registerReload() {
        propertyTriggeredExecutor.register(propertyName("property.reload.time.in.seconds"), scheduleReloading(), propertyValue("60"));
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
