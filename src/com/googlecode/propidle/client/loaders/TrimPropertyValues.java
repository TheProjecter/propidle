package com.googlecode.propidle.client.loaders;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.client.properties.Properties.properties;
import static com.googlecode.propidle.client.properties.Properties.toPairs;
import static com.googlecode.totallylazy.Pair.pair;

public class TrimPropertyValues implements Callable<Properties> {
    private final Callable<Properties> decorated;

    private TrimPropertyValues(Callable<Properties> decorated) {
        this.decorated = decorated;
    }

    public Properties call() throws Exception {
        return properties(toPairs(decorated.call()).map(trimPropertyValue()));
    }

    private Callable1<? super Pair<String, String>, Pair<String, String>> trimPropertyValue() {
        return new Callable1<Pair<String, String>, Pair<String, String>>() {
            public Pair<String, String> call(Pair<String, String> propertyKeyToValue) throws Exception {
                return pair(propertyKeyToValue.first(), propertyKeyToValue.second().trim());
            }
        };
    }

    public static TrimPropertyValues trimPropertyValues(Callable<Properties> decorated) {
        return new TrimPropertyValues(decorated);
    }
}