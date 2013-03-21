package com.googlecode.utterlyidle.migrations.persistence.jdbc;

import com.googlecode.utterlyidle.migrations.util.DriverManager;

import java.sql.Connection;

public class ConnectionDetails {
    private final String url;
    private final String user;
    private final String password;

    public static ConnectionDetails connectionDetails(String url, String user, String password) {
        return new ConnectionDetails(url, user, password);
    }

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
