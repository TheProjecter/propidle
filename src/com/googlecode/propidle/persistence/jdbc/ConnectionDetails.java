package com.googlecode.propidle.persistence.jdbc;

import com.googlecode.propidle.DriverManager;

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
        if (url().startsWith("jdbc:oracle:thin")) {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        return DriverManager.getConnection(url(), user(), password());
    }
}
