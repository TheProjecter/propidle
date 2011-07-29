package com.googlecode.propidle.util.tinytype;

import java.math.BigInteger;

public abstract class BigIntegerTinyType <T extends TinyType<BigInteger,?>> extends TinyType<BigInteger, T>{
    protected BigIntegerTinyType(BigInteger value) {
        super(value);
    }
}
