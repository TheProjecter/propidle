package com.googlecode.propidle.migrations.bootstrap.hsql;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.lazyrecords.sql.SqlSchema;
import com.googlecode.propidle.migrations.bootstrap.SqlBootstrapper;
import com.googlecode.propidle.migrations.persistence.jdbc.hsql.HsqlSqlDialect;

public class HsqlCreateMigrationLogTable extends SqlBootstrapper {

    public HsqlCreateMigrationLogTable(SqlRecords records, SqlSchema sqlSchema) {
        super(records, sqlSchema, new HsqlSqlDialect());
    }
}