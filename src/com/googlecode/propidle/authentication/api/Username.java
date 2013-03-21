package com.googlecode.propidle.authentication.api;

import com.googlecode.propidle.util.tinytype.StringTinyType;

public class Username extends StringTinyType<Username> {

    public Username(String value) {
        super(value);
    }

    public static Username username(String value) {
         return new Username(value);
    }

}
