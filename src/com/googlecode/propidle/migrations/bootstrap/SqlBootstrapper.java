package com.googlecode.propidle.migrations.bootstrap;

import static com.googlecode.propidle.migrations.log.MigrationLogFromRecords.MIGRATION_LOG;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.sql.SqlRecords;

import static java.lang.String.format;
import java.util.Iterator;

public class SqlBootstrapper implements Bootstrapper {
    protected final SqlRecords records;
    private final String checkTableExistsSql;

    public SqlBootstrapper(SqlRecords records, final String checkTableExistsSql) {
        this.records = records;
        this.checkTableExistsSql = checkTableExistsSql;
    }

    public void run() {
        if (tableExists()) {
            return;
        }
        String createMigrationLogSql = Strings.toString(SqlBootstrapper.class.getResourceAsStream("create_migration_log.sql"));
        try {
            records.update(createMigrationLogSql);
        } catch (Exception e) {
            throw new RuntimeException(format("Problem create migration logs using sql:\n%s", createMigrationLogSql), e);
        }
    }

    private boolean tableExists() {
        try {
            Iterator<Record> table = records.query(checkTableExistsSql, sequence(MIGRATION_LOG.toString()));
            return table.hasNext();
        } catch (Exception e) {
            throw new RuntimeException(format("Problem running sql to check if table exists:\n%s", checkTableExistsSql), e);
        }
    }
}
