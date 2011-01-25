package com.googlecode.propidle.search;

public class SearchException extends RuntimeException {
    public SearchException(String message, Exception cause) {
        super(message, cause);
    }
}
