package com.googlecode.propidle.persistence.jdbc;

import com.googlecode.propidle.DriverManager;

import java.sql.Connection;

public abstract class ConnectionDetails {
    private final String url;
    private final String user;
    private final String password;

    protected ConnectionDetails(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String url() {
        return url;
    }

    public String user() {
        return user;
    }

    public String password() {
        return password;
    }

    public Connection openConnection() throws Exception {
        return DriverManager.getConnection(url(), user(), password());
    }
}
