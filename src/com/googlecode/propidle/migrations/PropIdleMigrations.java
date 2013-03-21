package com.googlecode.propidle.migrations;


import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.propidle.migrations.Migrations;
import com.googlecode.propidle.migrations.ModuleMigrations;
import com.googlecode.propidle.migrations.ModuleName;
import com.googlecode.propidle.migrations.persistence.jdbc.SqlDialect;

import static com.googlecode.propidle.migrations.sql.SqlMigrations.sqlMigrationsInSamePackageAs;

public class PropIdleMigrations implements ModuleMigrations {
    public static final ModuleName moduleName = ModuleName.moduleName("Propidle Core");
    private final SqlRecords records;
    private final SqlDialect sqlDialect;

    public PropIdleMigrations(SqlRecords records, SqlDialect sqlDialect) {
        this.records = records;
        this.sqlDialect = sqlDialect;
    }

    public ModuleName moduleName() {
        return moduleName;
    }

    public Migrations migrations() {
        return Migrations.migrations(sqlMigrationsInSamePackageAs(PropIdleMigrations.class, records, sqlDialect));
    }
}
