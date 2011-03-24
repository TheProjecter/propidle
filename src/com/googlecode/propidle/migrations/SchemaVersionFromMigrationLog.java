package com.googlecode.propidle.migrations;

import com.googlecode.utterlyidle.migrations.log.MigrationLog;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Exceptions;
import com.googlecode.totallylazy.LazyException;

import static com.googlecode.utterlyidle.migrations.log.MigrationLogFromRecords.databaseSchemaVersion;
import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Right.right;

public class SchemaVersionFromMigrationLog implements SchemaVersion {
    private final MigrationLog migrationLog;

    public SchemaVersionFromMigrationLog(MigrationLog migrationLog) {
        this.migrationLog = migrationLog;
    }

    public Either<Throwable, Integer> versionNumber() throws Exception {
        try {
            return right(databaseSchemaVersion(migrationLog).value());
        } catch (LazyException e) {
            return left(Exceptions.getCause().call(e));
        }
    }
}
