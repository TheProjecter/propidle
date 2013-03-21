package com.googlecode.propidle.migrations.pretend.module1;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.propidle.migrations.Migrations;
import com.googlecode.propidle.migrations.ModuleMigrations;
import com.googlecode.propidle.migrations.ModuleName;
import com.googlecode.propidle.migrations.persistence.jdbc.SqlDialect;

import static com.googlecode.propidle.migrations.sql.SqlMigrations.sqlMigrationsInSamePackageAs;

public class Module1Migrations extends Migrations implements ModuleMigrations {

    public Module1Migrations(SqlRecords records, SqlDialect sqlDialect) {
        super(migrations(sqlMigrationsInSamePackageAs(Module1Migrations.class, records, sqlDialect)));
    }

    public ModuleName moduleName() {
        return ModuleName.moduleName("Module1");
    }

    public Migrations migrations() {
        return this;
    }
}
