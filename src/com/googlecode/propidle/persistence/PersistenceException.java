package com.googlecode.propidle.persistence;

public class PersistenceException extends RuntimeException{
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
