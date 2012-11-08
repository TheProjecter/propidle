package com.googlecode.propidle.client.loaders;

import com.googlecode.totallylazy.*;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.properties.Properties.toPairs;
import static com.googlecode.totallylazy.Sequences.sequence;

public final class ResolveVariables implements Callable<Properties> {
    private final Callable<Properties> loader;
    private final Sequence<String> propertyNames;

    public static ResolveVariables resolveProperties(Callable<Properties> loader, String propertyName, String... additionalPropertyNames) {
        return new ResolveVariables(loader, sequence(propertyName).join(sequence(additionalPropertyNames)));
    }

    private ResolveVariables(Callable<Properties> loader, Sequence<String> propertyNames) {
        this.loader = loader;
        this.propertyNames = propertyNames;
    }

    public Properties call() throws Exception {
        Properties properties = loader.call();
        return propertyNames.fold(properties, resolvePropertyName());
    }

    private static Callable2<? super Properties, ? super String, Properties> resolvePropertyName() {
        return new Callable2<Properties, String, Properties>() {
            public Properties call(Properties properties, String propertyName) throws Exception {
                if(propertyIsUsed(properties, resolvablePropertyName(propertyName))) {
                    checkThatPropertyIsDefined(properties, propertyName);
                    return toPairs(properties).fold(properties, resolve(propertyName));
                }

                return properties;
            }
        };
    }

    private static Callable2<? super Properties, ? super Pair<String, String>, Properties> resolve(final String propertyName) {
        return new Callable2<Properties, Pair<String, String>, Properties>() {
            public Properties call(Properties properties, Pair<String, String> property) throws Exception {
                if(property.second().contains(resolvablePropertyName(propertyName))){
                    properties.setProperty(property.first(), resolveProperty(properties, property.second(), propertyName));
                }
                return properties;
            }
        };
    }

    private static boolean propertyIsUsed(Properties properties, final String propertyName) {
        return toPairs(properties).map(Callables.<String>second()).exists(Strings.contains(propertyName));
    }

    private static String resolvablePropertyName(String propertyName) {
        return String.format("${%s}", propertyName);
    }

    private static void checkThatPropertyIsDefined(Properties properties, String propertyName) {
        if (!containsProperty(properties, propertyName)) {
            throw new IllegalArgumentException(String.format("Property %s not found in %s", propertyName, properties));
        }
    }

    private static boolean containsProperty(Properties properties, String property) {
        return properties.getProperty(property) != null;
    }

    private static String resolveProperty(Properties properties, String resolvablePropertyValue, String propertyName) {
        return resolvablePropertyValue.replace(resolvablePropertyName(propertyName), properties.getProperty(propertyName));
    }
}