package com.googlecode.propidle.migrations;

import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.migrations.history.MigrationEvent;
import com.googlecode.propidle.migrations.history.MigrationHistory;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Runnable1;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;

public class HistoryCheckingMigrator implements Migrator {
    private final MigrationHistory migrationHistory;
    private final Clock clock;

    public HistoryCheckingMigrator(MigrationHistory migrationHistory, Clock clock) {
        this.migrationHistory = migrationHistory;
        this.clock = clock;
    }

    public Iterable<MigrationEvent> migrate(Iterable<Migration> migrations) {
        Sequence<Migration> migrationsToRun = sequence(migrations).filter(notYetRun()).sortBy(Migration.getMigrationNumber());
        migrationsToRun.forEach(run());
        return migrationHistory.add(migrationsToRun.map(toMigrationEvent()));
    }

    private Runnable1<Migration> run() {
        return new Runnable1<Migration>() {
            public void run(Migration sqlMigration) {
                sqlMigration.run();
            }
        };
    }

    private Callable1<? super Migration, MigrationEvent> toMigrationEvent() {
        return new Callable1<Migration, MigrationEvent>() {
            public MigrationEvent call(Migration migration) throws Exception {
                return new MigrationEvent(clock.time(), migration);
            }
        };
    }

    private Predicate<? super Migration> notYetRun() {
        return new Predicate<Migration>() {
            public boolean matches(Migration migration) {
                return migrationHistory.get(migration.number()).isEmpty();
            }
        };
    }
}
