package com.googlecode.propidle.client.loaders;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.properties.Properties.toPairs;

public final class ResolveVariables implements Callable<Properties> {
    private final Callable<Properties> loader;
    private final String[] propertyNames;

    public static ResolveVariables resolveProperties(Callable<Properties> loader, String ... propertyNames) {
        return new ResolveVariables(loader, propertyNames);
    }

    private ResolveVariables(Callable<Properties> loader, String ... propertyNames) {
        checkPropertyNamesExist(propertyNames);

        this.loader = loader;
        this.propertyNames = propertyNames;
    }

    public Properties call() throws Exception {
        Properties properties = loader.call();
        return toPairs(properties).fold(properties, resolveVariables(propertyNames));
    }

    private static Callable2<? super Properties, ? super Pair<String, String>, Properties> resolveVariables(final String ... propertyNames) {
        return new Callable2<Properties, Pair<String, String>, Properties>() {
            public Properties call(Properties properties, Pair<String, String> property) throws Exception {
                for (String propertyName : propertyNames) {
                    checkThatPropertyExists(properties, propertyName);

                    if(property.second().contains(resolvablePropertyName(propertyName))){
                        properties.setProperty(property.first(), resolveProperty(properties, property.second(), propertyName));
                    }
                }
                return properties;
            }
        };
    }

    private static String resolvablePropertyName(String propertyName) {
        return String.format("${%s}", propertyName);
    }

    private static void checkThatPropertyExists(Properties properties, String propertyName) {
        if (!containsProperty(properties, propertyName)) {
            throw new IllegalArgumentException(String.format("Property %s not found in %s", propertyName, properties));
        }
    }

    private static void checkPropertyNamesExist(String[] propertyNames) {
        if (propertyNames == null || propertyNames.length == 0) {
            throw new IllegalArgumentException("No property names were specified.");
        }
    }

    private static boolean containsProperty(Properties properties, String property) {
        return properties.getProperty(property) != null;
    }

    private static String resolveProperty(Properties properties, String resolvablePropertyValue, String propertyName) {
        return resolvablePropertyValue.replace(resolvablePropertyName(propertyName), properties.getProperty(propertyName));
    }
}