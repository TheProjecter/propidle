package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.history.MigrationEvent;
import com.googlecode.propidle.persistence.RecordLock;

import static com.googlecode.propidle.migrations.history.MigrationHistoryFromRecords.MIGRATION_EVENTS;

public class LockMigrationsRecord implements Migrator {
    private final Migrator decorated;
    private final RecordLock recordLock;

    public LockMigrationsRecord(Migrator decorated, RecordLock recordLock) {
        this.decorated = decorated;
        this.recordLock = recordLock;
    }

    public Iterable<MigrationEvent> migrate(Iterable<Migration> migrations) {
        recordLock.aquire(MIGRATION_EVENTS);
        return decorated.migrate(migrations);
    }
}
