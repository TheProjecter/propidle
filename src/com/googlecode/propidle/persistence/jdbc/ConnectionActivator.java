package com.googlecode.propidle.persistence.jdbc;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;

import java.sql.SQLException;
import java.util.concurrent.Callable;

public class ConnectionActivator implements Callable<Connection>, Closeable {
    private final ConnectionDetails connectionDetails;
    private Connection connection;

    public ConnectionActivator(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public Connection call() throws Exception {
        if(connection == null){
            connection = connectionDetails.openConnection();
        }
        return connection;
    }

    public void close() throws IOException {
        if(connection != null)
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
