package com.googlecode.propidle.util;

public class ThisShouldNeverHappenException extends RuntimeException {
    public ThisShouldNeverHappenException(String message,Throwable cause) {
        super(message,cause);
    }
}
