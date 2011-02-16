package com.googlecode.propidle.migrations.sql;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class AdminSqlExecutorActivator implements Callable<SqlExecutor>, Closeable {
    private final AdminConnectionDetails connectionDetails;
    private Connection connection;

    public AdminSqlExecutorActivator(AdminConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public AdminSqlExecutor call() throws Exception {
        connection = connectionDetails.openConnection();
        return new AdminSqlExecutor(connection);
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
