package com.googlecode.propidle.server.properties;

import com.googlecode.propidle.util.StringTinyType;

public class PropertiesInput extends StringTinyType<PropertiesInput> {
    public PropertiesInput(String value) {
        super(value);
    }

    public byte[] getBytes() {
        return value().getBytes();
    }
}
