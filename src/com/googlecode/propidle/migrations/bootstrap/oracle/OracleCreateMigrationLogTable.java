package com.googlecode.propidle.migrations.bootstrap.oracle;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.lazyrecords.sql.SqlSchema;
import com.googlecode.propidle.migrations.bootstrap.SqlBootstrapper;
import com.googlecode.propidle.migrations.persistence.jdbc.oracle.OracleSqlDialect;
import com.googlecode.propidle.migrations.bootstrap.SqlBootstrapper;
import com.googlecode.propidle.migrations.persistence.jdbc.oracle.OracleSqlDialect;

public class OracleCreateMigrationLogTable extends SqlBootstrapper {
    public OracleCreateMigrationLogTable(SqlRecords records, SqlSchema sqlSchema) {
        super(records, sqlSchema, new OracleSqlDialect());
    }
}
