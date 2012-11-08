package com.googlecode.propidle.client.loaders;

import static com.googlecode.propidle.client.properties.Properties.compose;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;

import java.util.Properties;
import java.util.concurrent.Callable;

public class ComposeProperties implements Callable<Properties> {
    private final Sequence<Callable<Properties>> properties;

    public static ComposeProperties composeProperties(Callable<Properties>... properties) {
        return composeProperties(sequence(properties));
    }

    public static ComposeProperties composeProperties(Iterable<Callable<Properties>> properties) {
        return new ComposeProperties(properties);
    }

    protected ComposeProperties(Iterable<Callable<Properties>> properties) {
        this.properties = sequence(properties);
    }

    public Properties call() throws Exception {
        return compose(properties.map(Callables.<Properties>call()));
    }
}
