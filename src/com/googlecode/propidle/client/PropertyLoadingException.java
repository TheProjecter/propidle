package com.googlecode.propidle.client;

public class PropertyLoadingException extends RuntimeException {
    public PropertyLoadingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
