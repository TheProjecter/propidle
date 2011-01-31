package com.googlecode.propidle.util;

import com.googlecode.totallylazy.Predicate;

public class Predicates {
    public static Predicate<? super String> nonEmpty() {
        return new Predicate<String>() {
            public boolean matches(String other) {
                return other != null && other.trim().length() > 0;
            }
        };
    }
}
