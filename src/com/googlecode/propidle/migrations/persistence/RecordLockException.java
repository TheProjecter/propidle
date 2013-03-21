package com.googlecode.propidle.migrations.persistence;

public class RecordLockException extends RuntimeException {
    public RecordLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
