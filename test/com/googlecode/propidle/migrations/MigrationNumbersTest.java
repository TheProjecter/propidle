package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.MigrationNumber;
import com.googlecode.propidle.migrations.ModuleName;
import com.googlecode.totallylazy.None;
import com.googlecode.totallylazy.Option;
import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.propidle.migrations.MigrationNumbers.databaseSchemaVersion;
import static com.googlecode.propidle.migrations.ModuleName.moduleName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MigrationNumbersTest {
    
    @Test
    public void defaultsMigrationNumberToZeroIfThereAreNoMigrations() {
        assertThat(databaseSchemaVersion(moduleName("whatever"), emptyMigrationLog()).toString(), is(equalTo("0000")));
    }

    private MigrationLog emptyMigrationLog() {
        return new MigrationLog() {
            public Option<MigrationLogItem> get(MigrationNumber migrationNumber, ModuleName moduleName) {
                return None.none();
            }

            public Iterable<MigrationLogItem> add(Iterable<MigrationLogItem> auditItems) {
                throw new UnsupportedOperationException();
            }

            public Iterable<MigrationLogItem> list() {
                return empty(MigrationLogItem.class);
            }
        };
    }
}
