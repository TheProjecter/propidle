package com.googlecode.propidle.client.loaders;

import static com.googlecode.propidle.Properties.properties;

import java.util.Properties;
import java.util.concurrent.Callable;

public class EnvironmentVariables implements Callable<Properties> {
    public static EnvironmentVariables environmentVariables(){
        return new EnvironmentVariables();
    }

    protected EnvironmentVariables() {
    }

    public Properties call() throws Exception {
        return properties(System.getenv());
    }
}
