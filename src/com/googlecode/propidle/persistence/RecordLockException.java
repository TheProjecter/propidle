package com.googlecode.propidle.persistence;

public class RecordLockException extends RuntimeException {
    public RecordLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
