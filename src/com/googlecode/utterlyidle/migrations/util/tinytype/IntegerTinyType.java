package com.googlecode.utterlyidle.migrations.util.tinytype;

public abstract class IntegerTinyType<T extends TinyType<Integer,?>> extends TinyType<Integer, T> {
    protected IntegerTinyType(int value) {
        super(value);
    }
}
