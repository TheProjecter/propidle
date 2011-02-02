package com.googlecode.propidle;

import com.googlecode.propidle.util.StringTinyType;
import com.googlecode.totallylazy.Callable1;

import static com.googlecode.totallylazy.Sequences.sequence;

public class PropertyName extends StringTinyType<PropertyName> {
    public static Iterable<PropertyName> propertyNames(String... names) {
        return sequence(names).map(propertyName());
    }

    public static Callable1<? super String, PropertyName> propertyName() {
        return new Callable1<String, PropertyName>() {
            public PropertyName call(String name) throws Exception {
                return propertyName(name);
            }
        };
    }

    public static PropertyName propertyName(String value) {
        return new PropertyName(value);
    }
    protected PropertyName(String value) {
        super(value);
    }
}
