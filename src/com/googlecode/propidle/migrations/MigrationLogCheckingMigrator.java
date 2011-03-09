package com.googlecode.propidle.migrations;

import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.Callable1;

import static com.googlecode.totallylazy.Sequences.sequence;

public class MigrationLogCheckingMigrator implements Migrator {
    private final MigrationLog migrationLog;
    private final Clock clock;

    public MigrationLogCheckingMigrator(MigrationLog migrationLog, Clock clock) {
        this.migrationLog = migrationLog;
        this.clock = clock;
    }

    public Iterable<MigrationLogItem> migrate(Iterable<Migration> migrations) {
        Sequence<Migration> migrationsToRun = sequence(migrations).filter(notYetRun()).sortBy(Migration.getMigrationNumber());
        migrationsToRun.forEach(Runnables.<Migration>run());
        return migrationLog.add(migrationsToRun.map(toMigrationEvent()));
    }

    private Callable1<? super Migration, MigrationLogItem> toMigrationEvent() {
        return new Callable1<Migration, MigrationLogItem>() {
            public MigrationLogItem call(Migration migration) throws Exception {
                return new MigrationLogItem(clock.time(), migration);
            }
        };
    }

    private Predicate<? super Migration> notYetRun() {
        return new Predicate<Migration>() {
            public boolean matches(Migration migration) {
                return migrationLog.get(migration.number()).isEmpty();
            }
        };
    }
}
