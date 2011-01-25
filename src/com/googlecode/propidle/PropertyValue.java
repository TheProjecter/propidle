package com.googlecode.propidle;

import com.googlecode.propidle.util.StringTinyType;

import static com.googlecode.totallylazy.Option.none;

public class PropertyValue extends StringTinyType<PropertyValue> {
    public static PropertyValue propertyValue(String value) {
        if (value == null) {
            return null;
        } else {
            return new PropertyValue(value);
        }
    }

    protected PropertyValue(String value) {
        super(value);
    }
}
