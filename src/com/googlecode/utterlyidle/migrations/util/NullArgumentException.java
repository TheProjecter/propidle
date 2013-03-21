package com.googlecode.utterlyidle.migrations.util;

public class NullArgumentException extends RuntimeException{
    public NullArgumentException(String argumentName) {
        super(String.format("Argument %s cannot be null", argumentName));
    }
}
