package com.googlecode.propidle.persistence.jdbc.hsql;

import com.googlecode.propidle.persistence.RecordLock;
import com.googlecode.propidle.persistence.RecordLockException;
import com.googlecode.propidle.persistence.jdbc.SqlRecordLock;
import com.googlecode.totallylazy.records.Keyword;

import java.sql.Connection;
import java.sql.SQLException;

public class HsqlRecordLock extends SqlRecordLock {

    public HsqlRecordLock(Connection connection) {
        super(connection);
    }

    protected String lockStatement(Keyword recordName) {
        return String.format("LOCK TABLE %s WRITE", recordName);
    }
}
