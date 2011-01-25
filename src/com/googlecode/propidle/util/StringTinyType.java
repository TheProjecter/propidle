package com.googlecode.propidle.util;

public abstract class StringTinyType<T extends TinyType<String,?>> extends TinyType<String, T>{
    protected StringTinyType(String value) {
        super(value);
    }
}
