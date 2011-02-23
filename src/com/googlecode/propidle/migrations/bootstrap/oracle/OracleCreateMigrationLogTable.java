package com.googlecode.propidle.migrations.bootstrap.oracle;

import com.googlecode.propidle.migrations.bootstrap.SqlBootstrapper;
import com.googlecode.totallylazy.records.sql.SqlRecords;

public class OracleCreateMigrationLogTable extends SqlBootstrapper {
    public OracleCreateMigrationLogTable(SqlRecords records) {
        super(records, "select * from all_objects where object_type = 'TABLE' and object_name = upper(?)");
    }
}
