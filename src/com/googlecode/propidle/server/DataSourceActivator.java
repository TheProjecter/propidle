package com.googlecode.propidle.server;

import com.googlecode.utterlyidle.migrations.persistence.jdbc.ConnectionDetails;
import org.hsqldb.jdbc.JDBCDataSource;

import javax.sql.DataSource;
import java.util.concurrent.Callable;

public class DataSourceActivator implements Callable<DataSource> {

    private final ConnectionDetails connectionDetails;

    public DataSourceActivator(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    @Override
    public DataSource call() throws Exception {
        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setDatabase(connectionDetails.url());
        dataSource.setUser(connectionDetails.user());
        dataSource.setPassword(connectionDetails.password());
        return dataSource;
    }
}
