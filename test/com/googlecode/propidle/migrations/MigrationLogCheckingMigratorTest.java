package com.googlecode.propidle.migrations;

import static com.googlecode.propidle.migrations.Migration.migration;
import static com.googlecode.propidle.migrations.MigrationId.migrationId;
import static com.googlecode.propidle.migrations.MigrationName.migrationName;
import static com.googlecode.propidle.migrations.MigrationNumber.migrationNumber;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.propidle.migrations.log.MigrationLogFromRecords;
import com.googlecode.propidle.util.time.StoppedClock;

import static com.googlecode.propidle.migrations.ModuleName.moduleName;
import static com.googlecode.propidle.util.time.StoppedClock.stoppedClock;
import static com.googlecode.totallylazy.Option.option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.memory.MemoryRecords;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MigrationLogCheckingMigratorTest {
    private final StoppedClock clock = stoppedClock();
    private final MigrationLog migrationLog = new MigrationLogFromRecords(new MemoryRecords());
    private final MigrationLogCheckingMigrator migrator = new MigrationLogCheckingMigrator(migrationLog, clock);

    @Test
    public void runsMigrationFilesInOrder() throws Exception {
        List<String> migrationsPerformed = new ArrayList<String>();

        Sequence<Migration> migrations = sequence(
                migration(migrationId(migrationNumber(2), migrationName("second migration")), record("second", migrationsPerformed)),
                migration(migrationId(migrationNumber(1), migrationName("first  migration")), record("first", migrationsPerformed)),
                migration(migrationId(migrationNumber(3), migrationName("third  migration")), record("third", migrationsPerformed))
        );

        migrator.migrate(migrations, moduleName(getClass().getSimpleName()));

        assertThat(migrationsPerformed, hasExactly("first", "second", "third"));
    }

    @Test
    public void recordsMigrationHistory() throws Exception {
        Sequence<Migration> migrationsPerformed = sequence(migration(migrationId(migrationNumber(1), migrationName("some migration")), doNothing()));

        migrator.migrate(migrationsPerformed, moduleName(getClass().getSimpleName()));

        Migration migration = migrationsPerformed.first();

        assertThat(
                migrationLog.get(migration.number()),
                is(option(new MigrationLogItem(clock.time(),  migration.number(), migration.name(),moduleName(getClass().getSimpleName())))));
    }

    @Test
    public void willNotRunTheSameMigrationTwice() throws Exception {
        List<String> migrationsPerformed = new ArrayList<String>();
        Sequence<Migration> files = sequence(migration(migrationId(migrationNumber(1), migrationName("some migration")), record("migration happened", migrationsPerformed)));

        migrator.migrate(files, moduleName(getClass().getSimpleName()));
        migrator.migrate(files, moduleName(getClass().getSimpleName()));

        assertThat(migrationsPerformed, hasExactly("migration happened"));
    }

    private Runnable record(final String message, final List<String> list) {
        return new Runnable() {
            public void run() {
                list.add(message);
            }
        };
    }

    private Runnable doNothing() {
        return new Runnable() {
            public void run() {
            }
        };
    }
}
