package com.googlecode.propidle.persistence.jdbc;

import static com.googlecode.propidle.properties.Properties.getOrFail;

import java.util.Properties;

public class MigrationConnectionDetails extends ConnectionDetails {
    public static final String URL = "migration.jdbc.url";
    public static final String USER = "migration.jdbc.user";
    public static final String PASSWORD = "migration.jdbc.password";

    public MigrationConnectionDetails(Properties properties) {
        super(getOrFail(properties, URL), getOrFail(properties, USER), getOrFail(properties, PASSWORD));
    }
}
