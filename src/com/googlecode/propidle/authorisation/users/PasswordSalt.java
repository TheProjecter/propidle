package com.googlecode.propidle.authorisation.users;

import com.googlecode.propidle.util.tinytype.StringTinyType;

public class PasswordSalt extends StringTinyType<PasswordSalt> {
    public static PasswordSalt passwordSalt(String value) {
        return new PasswordSalt(value);
    }

    private PasswordSalt(String value) {
        super(value);
    }
}
