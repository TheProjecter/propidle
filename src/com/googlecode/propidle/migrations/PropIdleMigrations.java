package com.googlecode.propidle.migrations;


import com.googlecode.totallylazy.records.sql.SqlRecords;
import com.googlecode.utterlyidle.migrations.Migrations;
import com.googlecode.utterlyidle.migrations.ModuleMigrations;
import com.googlecode.utterlyidle.migrations.ModuleName;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.SqlDialect;

import static com.googlecode.utterlyidle.migrations.sql.SqlMigrations.sqlMigrationsInSamePackageAs;

public class PropIdleMigrations implements ModuleMigrations {
    private final SqlRecords records;
    private final SqlDialect sqlDialect;

    public PropIdleMigrations(SqlRecords records, SqlDialect sqlDialect) {
        this.records = records;
        this.sqlDialect = sqlDialect;
    }

    public ModuleName moduleName() {
        return ModuleName.moduleName("Propidle Core");
    }

    public Migrations migrations() {
        return Migrations.migrations(sqlMigrationsInSamePackageAs(PropIdleMigrations.class, records, sqlDialect));
    }
}
