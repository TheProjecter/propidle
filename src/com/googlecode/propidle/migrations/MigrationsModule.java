package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.history.MigrationHistory;
import com.googlecode.propidle.migrations.history.MigrationHistoryFromRecords;
import com.googlecode.propidle.migrations.sql.SqlExecutor;
import com.googlecode.propidle.migrations.sql.AdminSqlExecutorActivator;
import com.googlecode.propidle.migrations.sql.AdminConnectionDetails;
import static com.googlecode.propidle.migrations.sql.AdminConnectionDetails.adminConnectionDetails;
import com.googlecode.propidle.server.EnsureMigrationHistoryRecordIsDefined;
import com.googlecode.propidle.persistence.jdbc.ConnectionDetails;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

public class MigrationsModule implements RequestScopedModule {
    private final AdminConnectionDetails adminConnection;

    public MigrationsModule(ConnectionDetails adminConnection) {
        this.adminConnection = adminConnectionDetails(adminConnection);
    }

    public Module addPerRequestObjects(Container container) {
        container.
                addInstance(AdminConnectionDetails.class, adminConnection).
                add(MigrationHistory.class, MigrationHistoryFromRecords.class).
                add(Migrator.class, HistoryCheckingMigrator.class).
                decorate(Migrator.class, LockMigrationsRecord.class).
                decorate(Migrator.class, EnsureMigrationHistoryRecordIsDefined.class).
                addActivator(SqlExecutor.class, AdminSqlExecutorActivator.class).
                add(Migrations.class, PropIdleMigrations.class);
        return this;
    }
}
