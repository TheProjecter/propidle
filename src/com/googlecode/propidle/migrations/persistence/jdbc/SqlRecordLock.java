package com.googlecode.propidle.migrations.persistence.jdbc;

import com.googlecode.lazyrecords.Definition;
import com.googlecode.propidle.migrations.persistence.RecordLock;
import com.googlecode.propidle.migrations.persistence.RecordLockException;
import com.googlecode.propidle.migrations.persistence.RecordLock;
import com.googlecode.propidle.migrations.persistence.RecordLockException;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class SqlRecordLock implements RecordLock {
    protected final Connection connection;

    public SqlRecordLock(Connection connection) {
        this.connection = connection;
    }

    public void aquire(Definition recordName) {
        try {
            connection.setAutoCommit(false);
            connection.createStatement().execute(lockStatement(recordName));
        } catch (SQLException e) {
            throw new RecordLockException("Could not get lock on " + recordName, e);
        }
    }
    protected abstract String lockStatement(Definition recordName);
}
