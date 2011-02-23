package com.googlecode.propidle.migrations.bootstrap.hsql;

import com.googlecode.propidle.migrations.bootstrap.SqlBootstrapper;
import com.googlecode.totallylazy.records.sql.SqlRecords;

public class HsqlCreateMigrationLogTable extends SqlBootstrapper {

    public HsqlCreateMigrationLogTable(SqlRecords records) {
        super(records, "select * from INFORMATION_SCHEMA.tables where table_name = ucase(?);");
    }
}