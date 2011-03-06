package com.googlecode.propidle.persistence.jdbc;

import com.googlecode.propidle.persistence.RecordLock;
import com.googlecode.propidle.persistence.RecordLockException;
import com.googlecode.totallylazy.records.Keyword;

import java.sql.SQLException;
import java.sql.Connection;

public abstract class SqlRecordLock implements RecordLock{
    protected final Connection connection;

    public SqlRecordLock(Connection connection) {
        this.connection = connection;
    }

    public void aquire(Keyword recordName) {
        try {
            connection.setAutoCommit(false);
            connection.createStatement().execute(lockStatement(recordName));
        } catch (SQLException e) {
            throw new RecordLockException("Could not get lock on " + recordName, e);
        }
    }
    protected abstract String lockStatement(Keyword recordName);
}
