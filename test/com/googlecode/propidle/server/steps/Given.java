package com.googlecode.propidle.server.steps;

import com.googlecode.propidle.server.Values;

import java.util.concurrent.Callable;

public class Given<T> extends Step<T> {
    public Given(Class<? extends Callable<T>> aClass, Values values) {
        super(aClass, values);
    }
}
