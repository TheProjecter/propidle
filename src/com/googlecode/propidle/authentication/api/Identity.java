package com.googlecode.propidle.authentication.api;

import com.googlecode.propidle.util.tinytype.StringTinyType;

public class Identity extends StringTinyType<Identity>{

    public static Identity identity(String value) {
        return new Identity(value);
    }

    private Identity(String value) {
        super(value);
    }
}
