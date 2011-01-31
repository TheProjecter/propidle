package com.googlecode.propidle.util;

public abstract class IntegerTinyType<T extends TinyType<Integer,?>> extends TinyType<Integer, T> {
    protected IntegerTinyType(int value) {
        super(value);
    }
}
