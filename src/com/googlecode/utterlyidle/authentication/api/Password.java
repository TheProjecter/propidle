package com.googlecode.utterlyidle.authentication.api;

import com.googlecode.utterlyidle.util.tinytype.StringTinyType;

public class Password extends StringTinyType<Password> {

    public Password(String value) {
        super(value);
    }

    public static Password password(String value) {
         return new Password(value);
    }
}
