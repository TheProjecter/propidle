package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.propidle.migrations.log.MigrationLogFromRecords;
import com.googlecode.propidle.migrations.sql.MigrationUserSqlExecutorActivator;
import com.googlecode.propidle.migrations.sql.SqlExecutor;
import com.googlecode.propidle.persistence.jdbc.MigrationConnectionDetails;
import com.googlecode.propidle.migrations.bootstrap.MigrationLogBootstrapper;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.yadic.Container;

public class MigrationsModule implements RequestScopedModule, ApplicationScopedModule {
    public MigrationsModule() {
    }

    public Module addPerRequestObjects(Container container) {
        container.
                add(MigrationLog.class, MigrationLogFromRecords.class).
                add(Migrator.class, MigrationLogCheckingMigrator.class).
                decorate(Migrator.class, LockMigrationLogRecord.class).
                decorate(Migrator.class, MigrationLogBootstrapper.class).
                addActivator(SqlExecutor.class, MigrationUserSqlExecutorActivator.class).
                add(Migrations.class, PropIdleMigrations.class);
        return this;
    }

    public Module addPerApplicationObjects(Container container) {
        container.add(MigrationConnectionDetails.class);
        return this;
    }
}
