package com.googlecode.utterlyidle.migrations.bootstrap.oracle;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.lazyrecords.sql.SqlSchema;
import com.googlecode.utterlyidle.migrations.bootstrap.SqlBootstrapper;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.oracle.OracleSqlDialect;

public class OracleCreateMigrationLogTable extends SqlBootstrapper {
    public OracleCreateMigrationLogTable(SqlRecords records, SqlSchema sqlSchema) {
        super(records, sqlSchema, new OracleSqlDialect());
    }
}
