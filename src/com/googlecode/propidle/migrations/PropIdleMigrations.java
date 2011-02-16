package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.sql.SqlExecutor;
import static com.googlecode.propidle.migrations.sql.SqlMigrations.sqlMigrationsInSamePackageAs;

public class PropIdleMigrations extends Migrations {
    public PropIdleMigrations(SqlExecutor executor) {
        super(migrations(sqlMigrationsInSamePackageAs(HistoryCheckingMigrator.class, executor)));
    }
}
