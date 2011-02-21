package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.history.MigrationHistory;
import com.googlecode.propidle.migrations.history.MigrationHistoryFromRecords;
import com.googlecode.propidle.migrations.sql.MigrationUserSqlExecutorActivator;
import com.googlecode.propidle.migrations.sql.SqlExecutor;
import com.googlecode.propidle.persistence.jdbc.MigrationConnectionDetails;
import com.googlecode.propidle.server.EnsureMigrationHistoryRecordIsDefined;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.yadic.Container;

public class MigrationsModule implements RequestScopedModule, ApplicationScopedModule {
    public MigrationsModule() {
    }

    public Module addPerRequestObjects(Container container) {
        container.
                add(MigrationHistory.class, MigrationHistoryFromRecords.class).
                add(Migrator.class, HistoryCheckingMigrator.class).
                decorate(Migrator.class, LockMigrationsRecord.class).
                decorate(Migrator.class, EnsureMigrationHistoryRecordIsDefined.class).
                addActivator(SqlExecutor.class, MigrationUserSqlExecutorActivator.class).
                add(Migrations.class, PropIdleMigrations.class);
        return this;
    }

    public Module addPerApplicationObjects(Container container) {
        container.add(MigrationConnectionDetails.class);
        return this;
    }
}
