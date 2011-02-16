package com.googlecode.propidle.persistence.jdbc;

import com.googlecode.totallylazy.records.Records;
import com.googlecode.totallylazy.records.sql.SqlRecords;

import java.util.concurrent.Callable;

public class SqlRecordsActivator implements Callable<SqlRecords> {
    private final ConnectionProvider connectionProvider;

    public SqlRecordsActivator(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public SqlRecords call() throws Exception {
        return new SqlRecords(connectionProvider.getConnection());
    }
}
