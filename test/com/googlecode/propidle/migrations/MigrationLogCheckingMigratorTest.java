package com.googlecode.propidle.migrations;

import com.googlecode.lazyrecords.memory.MemoryRecords;
import com.googlecode.propidle.migrations.Migration;
import com.googlecode.propidle.migrations.MigrationLogCheckingMigrator;
import com.googlecode.propidle.migrations.ModuleName;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.propidle.migrations.log.MigrationLogFromRecords;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import com.googlecode.propidle.migrations.util.time.StoppedClock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.propidle.migrations.Migration.migration;
import static com.googlecode.propidle.migrations.MigrationId.migrationId;
import static com.googlecode.propidle.migrations.MigrationName.migrationName;
import static com.googlecode.propidle.migrations.MigrationNumber.migrationNumber;
import static com.googlecode.propidle.migrations.ModuleName.moduleName;
import static com.googlecode.propidle.migrations.util.time.StoppedClock.stoppedClock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

        ModuleName moduleName = moduleName(getClass().getSimpleName());
        migrator.migrate(migrationsPerformed, moduleName);

        Migration migration = migrationsPerformed.first();

        assertThat(
                migrationLog.get(migration.number(), moduleName),
                is(option(new MigrationLogItem(clock.time(),  migration.number(), migration.name(), moduleName))));
    }

    @Test
    public void willNotRunTheSameMigrationTwiceInTheSameModule() throws Exception {
        List<String> migrationsPerformed = new ArrayList<String>();
        Sequence<Migration> files = sequence(migration(migrationId(migrationNumber(1), migrationName("some migration")), record("migration happened", migrationsPerformed)));

        migrator.migrate(files, moduleName(getClass().getSimpleName()));
        migrator.migrate(files, moduleName(getClass().getSimpleName()));

        assertThat(migrationsPerformed, hasExactly("migration happened"));
    }

    @Test
    public void willPermitTheSameMigrationNumberInDifferentModules() throws Exception {
        List<String> migrationsPerformed = new ArrayList<String>();
        Sequence<Migration> files = sequence(
                migration(migrationId(migrationNumber(1), migrationName("some migration")), record("migration happened", migrationsPerformed))
        );

        migrator.migrate(files, moduleName("Module1"));
        migrator.migrate(files, moduleName("Module2"));

        assertThat(migrationsPerformed, hasExactly("migration happened","migration happened"));
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
