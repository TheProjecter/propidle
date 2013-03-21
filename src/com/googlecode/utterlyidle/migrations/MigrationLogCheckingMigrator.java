package com.googlecode.utterlyidle.migrations;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.migrations.log.MigrationLog;
import com.googlecode.utterlyidle.migrations.log.MigrationLogItem;
import com.googlecode.utterlyidle.migrations.util.time.Clock;

import static com.googlecode.totallylazy.Sequences.sequence;

public class MigrationLogCheckingMigrator implements Migrator {
    private final MigrationLog migrationLog;
    private final Clock clock;

    public MigrationLogCheckingMigrator(MigrationLog migrationLog, Clock clock) {
        this.migrationLog = migrationLog;
        this.clock = clock;
    }

    public Iterable<MigrationLogItem> migrate(Iterable<Migration> migrations, final ModuleName moduleName) {
        Sequence<Migration> migrationsToRun = sequence(migrations).filter(notYetRun(moduleName)).sortBy(Migration.getMigrationNumber());
        migrationsToRun.forEach(Runnables.<Migration>run());
        return migrationLog.add(migrationsToRun.map(toMigrationEvent(moduleName)));
    }

    private Callable1<? super Migration, MigrationLogItem> toMigrationEvent(final ModuleName moduleName) {
        return new Callable1<Migration, MigrationLogItem>() {
            public MigrationLogItem call(Migration migration) throws Exception {
                return new MigrationLogItem(clock.time(), migration, moduleName);
            }
        };
    }

    private Predicate<? super Migration> notYetRun(final ModuleName moduleName) {
        return new Predicate<Migration>() {
            public boolean matches(Migration migration) {
                return migrationLog.get(migration.number(), moduleName).isEmpty();
            }
        };
    }
}
