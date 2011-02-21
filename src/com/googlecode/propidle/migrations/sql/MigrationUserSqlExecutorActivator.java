package com.googlecode.propidle.migrations.sql;

import com.googlecode.propidle.persistence.jdbc.MigrationConnectionDetails;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class MigrationUserSqlExecutorActivator implements Callable<SqlExecutor>, Closeable {
    private final MigrationConnectionDetails connectionDetails;
    private Connection connection;

    public MigrationUserSqlExecutorActivator(MigrationConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public MigrationUserSqlExecutor call() throws Exception {
        connection = connectionDetails.openConnection();
        return new MigrationUserSqlExecutor(connection);
    }

    public void close() throws IOException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new IOException("Could not close admin connection", e);
            }
        }
    }
}
