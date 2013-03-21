package com.googlecode.propidle.migrations.persistence.jdbc.hsql;

import com.googlecode.lazyrecords.Definition;
import com.googlecode.propidle.migrations.persistence.jdbc.SqlRecordLock;

import java.sql.Connection;

import static com.googlecode.lazyrecords.sql.expressions.Expressions.tableName;

public class HsqlRecordLock extends SqlRecordLock {

    public HsqlRecordLock(Connection connection) {
        super(connection);
    }

    protected String lockStatement(Definition definition) {
        return String.format("LOCK TABLE %s WRITE", tableName(definition));
    }
}
