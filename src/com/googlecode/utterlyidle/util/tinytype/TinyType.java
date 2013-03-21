package com.googlecode.utterlyidle.util.tinytype;

import com.googlecode.utterlyidle.util.NullArgumentException;

public abstract class TinyType<T extends Comparable, SELF_CLASS extends TinyType<T, ? extends Object>> implements Comparable<SELF_CLASS> {
    private final T value;

    protected TinyType(T value) {
        if (value == null) throw new NullArgumentException("value");
        this.value = value;
    }

    public int compareTo(SELF_CLASS other) {
        return value.compareTo(other.value());
    }

    public T value() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        return getClass().isInstance(o) && ((TinyType) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
