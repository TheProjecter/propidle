package com.googlecode.utterlyidle.migrations;

import com.googlecode.utterlyidle.migrations.log.MigrationLogItem;
import com.googlecode.utterlyidle.migrations.persistence.RecordLock;

import static com.googlecode.utterlyidle.migrations.log.MigrationLogFromRecords.MIGRATION_LOG;

public class LockMigrationLogRecord implements Migrator {
    private final Migrator decorated;
    private final RecordLock recordLock;

    public LockMigrationLogRecord(Migrator decorated, RecordLock recordLock) {
        this.decorated = decorated;
        this.recordLock = recordLock;
    }

    public Iterable<MigrationLogItem> migrate(Iterable<Migration> migrations, final ModuleName moduleName) {
        recordLock.aquire(MIGRATION_LOG);
        return decorated.migrate(migrations, moduleName);
    }
}
