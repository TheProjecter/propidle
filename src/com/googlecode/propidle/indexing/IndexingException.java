package com.googlecode.propidle.indexing;

import com.googlecode.propidle.properties.PropertiesPath;

public class IndexingException extends RuntimeException {
    public IndexingException(String message, Exception cause) {
        super(message, cause);
    }

    public IndexingException(PropertiesPath path, Exception e) {
        this("Could not index " + path, e);
    }
}
