package com.googlecode.propidle.migrations.pretend.module2;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.propidle.migrations.Migrations;
import com.googlecode.propidle.migrations.ModuleMigrations;
import com.googlecode.propidle.migrations.ModuleName;
import com.googlecode.propidle.migrations.persistence.jdbc.SqlDialect;

import static com.googlecode.propidle.migrations.sql.SqlMigrations.sqlMigrationsInSamePackageAs;

public class Module2Migrations extends Migrations implements ModuleMigrations {
    public Module2Migrations(SqlRecords records, SqlDialect sqlDialect) {
        super(migrations(sqlMigrationsInSamePackageAs(Module2Migrations.class, records, sqlDialect)));
    }

    public ModuleName moduleName() {
        return ModuleName.moduleName("Module2");
    }

    public Migrations migrations() {
        return this;
    }
}
