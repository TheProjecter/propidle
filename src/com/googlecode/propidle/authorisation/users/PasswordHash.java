package com.googlecode.propidle.authorisation.users;

import com.googlecode.propidle.util.StringTinyType;

public class PasswordHash extends StringTinyType<PasswordHash> {
    public static PasswordHash passwordHash(String value) {
        return new PasswordHash(value);
    }

    protected PasswordHash(String value) {
        super(value);
    }
}
