package com.googlecode.propidle.server.steps.givens;

import com.googlecode.propidle.AllProperties;
import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.server.Values;
import com.googlecode.propidle.server.steps.Given;

import java.util.Properties;
import java.util.concurrent.Callable;

public class PropertiesExist implements Callable<Properties> {
    private final AllProperties allProperties;
    private final PropertiesPath path;
    private final Properties properties;

    public PropertiesExist(AllProperties allProperties, PropertiesPath path, Properties properties) {
        this.allProperties = allProperties;
        this.path = path;
        this.properties = properties;
    }

    public static Given<Properties> propertiesExist(Values values){
        return new Given<Properties>(PropertiesExist.class, values);
    }
    public Properties call() throws Exception {
        allProperties.put(path, properties);
        return properties;
    }
}
