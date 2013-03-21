package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.log.MigrationLogFromRecords;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import com.googlecode.propidle.migrations.persistence.RecordLock;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import com.googlecode.propidle.migrations.persistence.RecordLock;

import static com.googlecode.propidle.migrations.log.MigrationLogFromRecords.MIGRATION_LOG;

public class LockMigrationLogRecord implements Migrator {
    private final Migrator decorated;
    private final RecordLock recordLock;

    public LockMigrationLogRecord(Migrator decorated, RecordLock recordLock) {
        this.decorated = decorated;
        this.recordLock = recordLock;
    }

    public Iterable<MigrationLogItem> migrate(Iterable<Migration> migrations, final ModuleName moduleName) {
        recordLock.aquire(MigrationLogFromRecords.MIGRATION_LOG);
        return decorated.migrate(migrations, moduleName);
    }
}
