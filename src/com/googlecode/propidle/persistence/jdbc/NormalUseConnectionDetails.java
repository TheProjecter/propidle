package com.googlecode.propidle.persistence.jdbc;

import static com.googlecode.propidle.properties.Properties.getOrFail;

import java.util.Properties;

public class NormalUseConnectionDetails extends ConnectionDetails{
    public static final String URL = "jdbc.url";
    public static final String USER = "jdbc.user";
    public static final String PASSWORD = "jdbc.password";

    public NormalUseConnectionDetails(Properties properties) {
        super(getOrFail(properties, URL), getOrFail(properties, USER), getOrFail(properties, PASSWORD));
    }
}
