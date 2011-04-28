package com.googlecode.propidle.client;

import java.util.Properties;
import java.util.concurrent.Callable;

public class DynamicPropertiesActivator implements Callable<DynamicProperties> {
    private final Callable<Properties> propertyLoader;

    public DynamicPropertiesActivator(Callable<Properties> propertyLoader) {
        this.propertyLoader = propertyLoader;
    }

    public DynamicProperties call() throws Exception {
        return DynamicProperties.load(propertyLoader);
    }
}
