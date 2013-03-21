package com.googlecode.utterlyidle.migrations.persistence.jdbc.hsql;

import com.googlecode.utterlyidle.migrations.persistence.RecordLock;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.SqlDialect;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

public class HsqlModule implements RequestScopedModule {
    public Container addPerRequestObjects(Container container) {
        return container.add(RecordLock.class, HsqlRecordLock.class).add(SqlDialect.class, HsqlSqlDialect.class);
    }
}