package com.googlecode.propidle.client.loaders;

import java.util.Properties;
import java.util.concurrent.Callable;

public class SystemProperties implements Callable<Properties> {
    public static SystemProperties systemProperties() {
        return new SystemProperties();
    }

    protected SystemProperties() {
    }

    public Properties call() throws Exception {
        return System.getProperties();
    }
}
