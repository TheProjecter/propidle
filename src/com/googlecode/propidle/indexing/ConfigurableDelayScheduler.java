package com.googlecode.propidle.indexing;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.propidle.client.SpecificPropertyChangeListener;
import com.googlecode.propidle.properties.PropertyComparison;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.totallylazy.Callable1;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.Long.valueOf;
import static java.util.concurrent.TimeUnit.MINUTES;

public class ConfigurableDelayScheduler {

    private final DynamicProperties dynamicProperties;
    private final ScheduledExecutorService executorService;
    private final Runnable task;
    private final PropertyName delayPropertyName;
    private ScheduledFuture<?> scheduledFuture;

    public ConfigurableDelayScheduler(ScheduledExecutorService executorService, Runnable task, DynamicProperties dynamicProperties, PropertyName delayPropertyName) {
        this.dynamicProperties = dynamicProperties;
        this.executorService = executorService;
        this.task = task;
        this.delayPropertyName = delayPropertyName;
    }

    public void schedule(){
        Long refreshDelay = valueOf(dynamicProperties.snapshot().getProperty(delayPropertyName.value(), "1"));
        scheduledFuture = executorService.scheduleWithFixedDelay(task, 0, refreshDelay, MINUTES);

        dynamicProperties.listen(new SpecificPropertyChangeListener(propertyName(delayPropertyName.value()), rescheduleTask(scheduledFuture, executorService)));
    }

    private Callable1<PropertyComparison, Void> rescheduleTask(final ScheduledFuture<?> scheduledFuture, final ScheduledExecutorService executorService) {
        return new Callable1<PropertyComparison, Void>() {
            public Void call(PropertyComparison propertyComparison) throws Exception {
                scheduledFuture.cancel(true);
                Long delay = valueOf(propertyComparison.updated().getOrElse(propertyValue("1")).value());
                executorService.scheduleWithFixedDelay(task, delay, delay, MINUTES);
                return null;
            }
        };
    }


}
