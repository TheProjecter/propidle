package com.googlecode.utterlyidle.migrations;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.migrations.log.MigrationLog;
import com.googlecode.utterlyidle.migrations.log.MigrationLogItem;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.migrations.MigrationNumber.migrationNumber;
import static com.googlecode.utterlyidle.migrations.log.MigrationLogItem.forModule;
import static com.googlecode.utterlyidle.migrations.log.MigrationLogItem.getMigrationNumber;

public class MigrationNumbers {
    public static MigrationNumber databaseSchemaVersion(final ModuleName moduleName, MigrationLog migrationLog) {
        Sequence<MigrationLogItem> orderedMigrations = sequence(migrationLog.list()).filter(forModule(moduleName)).sortBy(getMigrationNumber());
        return orderedMigrations.isEmpty() ? migrationNumber(0) : orderedMigrations.last().number();
    }
}
