package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.propidle.migrations.log.MigrationLogItem;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.propidle.migrations.MigrationNumber.migrationNumber;
import static com.googlecode.propidle.migrations.log.MigrationLogItem.forModule;
import static com.googlecode.propidle.migrations.log.MigrationLogItem.getMigrationNumber;

public class MigrationNumbers {
    public static MigrationNumber databaseSchemaVersion(final ModuleName moduleName, MigrationLog migrationLog) {
        Sequence<MigrationLogItem> orderedMigrations = sequence(migrationLog.list()).filter(MigrationLogItem.forModule(moduleName)).sortBy(MigrationLogItem.getMigrationNumber());
        return orderedMigrations.isEmpty() ? MigrationNumber.migrationNumber(0) : orderedMigrations.last().number();
    }
}
