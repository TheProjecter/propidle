package com.googlecode.propidle.persistence.jdbc.hsql;

import com.googlecode.propidle.persistence.RecordLock;
import com.googlecode.propidle.persistence.RecordLockException;
import com.googlecode.totallylazy.records.Keyword;

import java.sql.Connection;
import java.sql.SQLException;

public class HsqlRecordLock implements RecordLock {
    private final Connection connection;

    public HsqlRecordLock(Connection connection) {
        this.connection = connection;
    }

    public void aquire(Keyword recordName) {
        try {
            connection.setAutoCommit(false);
            connection.createStatement().execute(String.format("LOCK TABLE %s WRITE", recordName));
        } catch (SQLException e) {
            throw new RecordLockException("Could not get lock on " + recordName, e);
        }
    }
}
