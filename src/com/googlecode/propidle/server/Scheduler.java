package com.googlecode.propidle.server;

import com.googlecode.propidle.indexing.ConfigurableDelayScheduler;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;


public class Scheduler {

    private final Container applicationContainer;

    public Scheduler(Container applicationContainer) {
        this.applicationContainer = applicationContainer;
    }

    public void schedule(final Class<? extends Runnable> taskClass, final PropertyName delayPropertyName) {
        SimpleContainer container = new SimpleContainer(applicationContainer);
        container.add(Runnable.class, taskClass);
        container.addInstance(PropertyName.class, delayPropertyName);
        container.add(ConfigurableDelayScheduler.class);
        container.get(ConfigurableDelayScheduler.class).schedule();
    }

}
