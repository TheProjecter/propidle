package com.googlecode.propidle.util.tinytype;

public abstract class StringTinyType<T extends TinyType<String,?>> extends TinyType<String, T>{
    protected StringTinyType(String value) {
        super(value);
    }
}
