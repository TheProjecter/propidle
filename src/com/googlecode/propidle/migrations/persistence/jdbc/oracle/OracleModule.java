package com.googlecode.propidle.migrations.persistence.jdbc.oracle;

import com.googlecode.propidle.migrations.persistence.RecordLock;
import com.googlecode.propidle.migrations.persistence.jdbc.SqlDialect;
import com.googlecode.propidle.migrations.persistence.RecordLock;
import com.googlecode.propidle.migrations.persistence.jdbc.SqlDialect;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

public class OracleModule implements RequestScopedModule {
    public Container addPerRequestObjects(Container container) {
        return container.add(RecordLock.class, OracleRecordLock.class).add(SqlDialect.class, OracleSqlDialect.class);
    }
}