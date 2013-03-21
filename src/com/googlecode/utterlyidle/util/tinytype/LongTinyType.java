package com.googlecode.utterlyidle.util.tinytype;

public abstract class LongTinyType<T extends TinyType<Long,?>> extends TinyType<Long, T>{
    protected LongTinyType(Long value) {
        super(value);
    }
}
