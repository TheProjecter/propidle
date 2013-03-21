package com.googlecode.utterlyidle.migrations.bootstrap;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.lazyrecords.sql.SqlSchema;
import com.googlecode.totallylazy.Strings;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.SqlDialect;
import com.googlecode.utterlyidle.migrations.sql.SqlFile;
import com.googlecode.utterlyidle.migrations.sql.SqlLine;

import static com.googlecode.lazyrecords.sql.expressions.Expressions.textOnly;
import static com.googlecode.utterlyidle.migrations.log.MigrationLogFromRecords.MIGRATION_LOG;
import static java.lang.String.format;

public class SqlBootstrapper implements Bootstrapper {
    protected final SqlRecords records;
    private final SqlDialect sqlDialect;
    private final SqlSchema sqlSchema;

    public SqlBootstrapper(SqlRecords records, SqlSchema sqlSchema, SqlDialect sqlDialect) {
        this.records = records;
        this.sqlDialect = sqlDialect;
        this.sqlSchema = sqlSchema;
    }

    public void run() {
        if (sqlSchema.exists(MIGRATION_LOG)) {
            return;
        }
        SqlFile createMigrationLogSql = SqlFile.sqlFile(Strings.toString(SqlBootstrapper.class.getResourceAsStream(sqlDialect.name() + "/" +"create_migration_log.sql")));
        Iterable<SqlLine> sqlLines = sqlDialect.linesIn(createMigrationLogSql);

        for (SqlLine sqlLine : sqlLines) {
            try {
                records.update(textOnly(sqlLine.value()));
            } catch (Exception e) {
                throw new RuntimeException(format("Problem create migration logs using sql:\n%s", sqlLine), e);
            }
        }
    }
}
