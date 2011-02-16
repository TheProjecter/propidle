package com.googlecode.propidle.authorisation.users;

import com.googlecode.propidle.util.tinytype.BigIntegerTinyType;

import java.math.BigInteger;

public class PasswordHash extends BigIntegerTinyType<PasswordHash> {
    public static PasswordHash passwordHash(Number value) {
        return new PasswordHash(new BigInteger(value.toString()));
    }

    protected PasswordHash(BigInteger value) {
        super(value);
    }
}
