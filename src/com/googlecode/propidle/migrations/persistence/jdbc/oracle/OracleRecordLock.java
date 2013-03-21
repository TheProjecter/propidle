package com.googlecode.propidle.migrations.persistence.jdbc.oracle;

import com.googlecode.lazyrecords.Definition;
import com.googlecode.propidle.migrations.persistence.jdbc.SqlRecordLock;

import java.sql.Connection;

import static com.googlecode.lazyrecords.sql.expressions.Expressions.tableName;

public class OracleRecordLock extends SqlRecordLock {
    public OracleRecordLock(Connection connection) {
        super(connection);
    }

    protected String lockStatement(Definition recordName) {
        return String.format("LOCK TABLE %s IN EXCLUSIVE MODE", tableName(recordName));
    }
}