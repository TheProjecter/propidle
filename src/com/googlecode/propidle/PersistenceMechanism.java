package com.googlecode.propidle;

import java.util.Properties;

public enum PersistenceMechanism {
    HSQL,
    ORACLE,
    IN_MEMORY;
    public static final String PERSISTENCE = "persistence";

    public static PersistenceMechanism fromProperties(Properties properties) {
        return parse(com.googlecode.propidle.client.properties.Properties.getOrFail(properties, PERSISTENCE));
    }

    public static PersistenceMechanism parse(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(java.lang.String.format("'%s' is not a supported persistence method", value), e);
        }
    }
}
