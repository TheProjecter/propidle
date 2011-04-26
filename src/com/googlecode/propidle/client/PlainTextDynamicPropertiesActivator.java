package com.googlecode.propidle.client;

import java.util.Properties;
import java.util.concurrent.Callable;

public class PlainTextDynamicPropertiesActivator implements Callable<DynamicProperties> {
    private final Callable<Properties> propertyLoader;

    public PlainTextDynamicPropertiesActivator(Callable<Properties> propertyLoader) {
        this.propertyLoader = propertyLoader;
    }

    public DynamicProperties call() throws Exception {
        return PlainTextDynamicProperties.load(propertyLoader);
    }
}
