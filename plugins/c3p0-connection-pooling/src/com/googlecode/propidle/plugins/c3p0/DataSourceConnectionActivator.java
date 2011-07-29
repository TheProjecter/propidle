package com.googlecode.propidle.plugins.c3p0;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class DataSourceConnectionActivator implements Callable<Connection>, Closeable {
    private final DataSource dataSource;
    private Connection connection;

    public DataSourceConnectionActivator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection call() throws Exception {
        if(connection == null){
            connection = dataSource.getConnection();
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
