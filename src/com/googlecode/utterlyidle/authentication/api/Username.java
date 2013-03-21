package com.googlecode.utterlyidle.authentication.api;

import com.googlecode.utterlyidle.util.tinytype.StringTinyType;

public class Username extends StringTinyType<Username> {

    public Username(String value) {
        super(value);
    }

    public static Username username(String value) {
         return new Username(value);
    }

}
