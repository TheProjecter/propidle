package com.googlecode.propidle.server.steps;

import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.propidle.server.Values;
import static com.googlecode.propidle.server.Values.noValues;

import java.util.concurrent.Callable;

public class Step<T> implements Callable1<Container,T> {
    protected final Class<? extends Callable<T>> aClass;
    protected final Values values;

    public Step(Class<? extends Callable<T>> aClass) {
        this(aClass, noValues());
    }

    public Step(Class<? extends Callable<T>> aClass, Values values) {
        this.aClass = aClass;
        this.values = values;
    }

    public T call(Container baseContainer) throws Exception {
        SimpleContainer resolver = new SimpleContainer(baseContainer);
        for (Object value : values) {
            addInstance(resolver, value);
        }
        resolver.add(aClass);
        return resolver.get(aClass).call();
    }

    private <I,C extends I> void addInstance(SimpleContainer resolver, Object instance) {
        Class<I> castClass= (Class<I>)instance.getClass();
        C castInstance = (C) instance;
        resolver.addInstance(castClass, castInstance);
    }
}
