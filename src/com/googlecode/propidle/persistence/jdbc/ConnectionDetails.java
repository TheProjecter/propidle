package com.googlecode.propidle.persistence.jdbc;

import com.googlecode.propidle.DriverManager;
import static com.googlecode.propidle.properties.Properties.getOrFail;

import java.sql.Connection;
import java.util.Properties;

public class ConnectionDetails {
    public static final String URL = "jdbc.url";
    public static final String USER = "jdbc.user";
    public static final String PASSWORD = "jdbc.password";

    private final String url;
    private final String user;
    private final String password;

    public static ConnectionDetails connectionDetails(Properties properties) {
        return new ConnectionDetails(properties);
    }

    public static ConnectionDetails connectionDetails(String url, String user, String password) {
        return new ConnectionDetails(url, user, password);
    }

    protected ConnectionDetails(Properties properties) {
        this(getOrFail(properties, URL), getOrFail(properties, USER), getOrFail(properties, PASSWORD));
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
        if(url().startsWith("jdbc:oracle:thin")){
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        return DriverManager.getConnection(url(), user(), password());
    }
}
