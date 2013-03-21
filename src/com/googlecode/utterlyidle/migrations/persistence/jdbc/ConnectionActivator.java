package com.googlecode.utterlyidle.migrations.persistence.jdbc;

import com.googlecode.totallylazy.Closeables;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.util.concurrent.Callable;

public class ConnectionActivator implements Callable<Connection>, Closeable {
    private final ConnectionDetails connectionDetails;
    private Connection connection;

    public ConnectionActivator(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public Connection call() throws Exception {
        return connection = connectionDetails.openConnection();
    }

    public void close() throws IOException {
        Closeables.close(connection);
    }
}
