package com.googlecode.propidle.migrations;

import static com.googlecode.propidle.migrations.sql.SqlMigrations.sqlMigrationsInSamePackageAs;
import com.googlecode.totallylazy.records.sql.SqlRecords;

public class PropIdleMigrations extends Migrations {
    public PropIdleMigrations(SqlRecords records) {
        super(migrations(sqlMigrationsInSamePackageAs(PropIdleMigrations.class, records)));
    }
}
