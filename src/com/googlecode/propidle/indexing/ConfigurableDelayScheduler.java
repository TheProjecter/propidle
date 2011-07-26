package com.googlecode.propidle.indexing;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.propidle.client.SpecificPropertyChangeListener;
import com.googlecode.propidle.properties.PropertyComparison;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.propidle.scheduling.RunnableRequest;
import com.googlecode.propidle.scheduling.SchedulableTask;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.Application;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static java.lang.Long.valueOf;
import static java.util.concurrent.TimeUnit.MINUTES;

public class ConfigurableDelayScheduler {

    private final DynamicProperties dynamicProperties;
    private final ScheduledExecutorService executorService;
    private final Application application;
    private final PropertyName delayPropertyName;

    public ConfigurableDelayScheduler(ScheduledExecutorService executorService, DynamicProperties dynamicProperties, Application application, PropertyName delayPropertyName) {
        this.dynamicProperties = dynamicProperties;
        this.executorService = executorService;
        this.application = application;
        this.delayPropertyName = delayPropertyName;
    }

    public ScheduledFuture<?> schedule(SchedulableTask task){
        Long refreshDelay = valueOf(dynamicProperties.snapshot().getProperty(delayPropertyName.value(), "1"));
        return executorService.scheduleWithFixedDelay(toRunnable(task), 0, refreshDelay, MINUTES);
    }

    private RunnableRequest toRunnable(SchedulableTask task) {
        return new RunnableRequest(application, task.request());
    }
}
