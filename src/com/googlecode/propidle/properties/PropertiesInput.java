package com.googlecode.propidle.properties;

import com.googlecode.propidle.util.tinytype.StringTinyType;

public class PropertiesInput extends StringTinyType<PropertiesInput> {
    public PropertiesInput(String value) {
        super(value);
    }

    public byte[] getBytes() {
        return value().getBytes();
    }
}
