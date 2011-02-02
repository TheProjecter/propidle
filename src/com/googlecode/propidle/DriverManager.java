package com.googlecode.propidle;

import org.hsqldb.jdbcDriver;

import java.sql.Connection;
import java.sql.Driver;

public class DriverManager {

    public static Connection getConnection(String url, String username, String password) throws Exception {
        java.sql.DriverManager.registerDriver((Driver) Class.forName(jdbcDriver.class.getName()).newInstance());
        return java.sql.DriverManager.getConnection(url,
                                           username,
                                           password);
    }
}
