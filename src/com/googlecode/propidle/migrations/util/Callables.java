package com.googlecode.propidle.migrations.util;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;

public class Callables {
    public static <T> Callable2<T, Callable1<T, T>, T> chain(Class<T> aClass) {
        return chain();
    }

    public static <T> Callable2<T, Callable1<T, T>, T> chain() {
        return new Callable2<T, Callable1<T, T>, T>() {
            public T call(T t, Callable1<T, T> callable) throws Exception {
                return callable.call(t);
            }
        };
    }
}
