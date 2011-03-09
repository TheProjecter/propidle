package com.googlecode.propidle.client;

import java.util.Properties;
import java.util.concurrent.Callable;

public class SnapshotPropertiesActivator implements Callable<Properties> {
    private final DynamicProperties dynamicProperties;

    public SnapshotPropertiesActivator(DynamicProperties dynamicProperties) {
        this.dynamicProperties = dynamicProperties;
    }

    public Properties call() throws Exception {
        return dynamicProperties.snapshot();
    }
}
