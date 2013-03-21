package com.googlecode.utterlyidle.migrations.bootstrap.hsql;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.lazyrecords.sql.SqlSchema;
import com.googlecode.utterlyidle.migrations.bootstrap.SqlBootstrapper;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.hsql.HsqlSqlDialect;

public class HsqlCreateMigrationLogTable extends SqlBootstrapper {

    public HsqlCreateMigrationLogTable(SqlRecords records, SqlSchema sqlSchema) {
        super(records, sqlSchema, new HsqlSqlDialect());
    }
}