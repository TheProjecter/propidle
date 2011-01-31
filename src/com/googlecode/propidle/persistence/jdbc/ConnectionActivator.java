package com.googlecode.propidle.persistence.jdbc;

import java.sql.Connection;
import com.googlecode.propidle.DriverManager;

import java.util.concurrent.Callable;

public class ConnectionActivator implements Callable<Connection> {
    private final ConnectionDetails connectionDetails;

    public ConnectionActivator(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public Connection call() throws Exception {
        return DriverManager.getConnection(connectionDetails.url(),
                                           connectionDetails.user(),
                                           connectionDetails.password());
    }
}
