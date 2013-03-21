package com.googlecode.utterlyidle.migrations.pretend.module1;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.utterlyidle.migrations.Migrations;
import com.googlecode.utterlyidle.migrations.ModuleMigrations;
import com.googlecode.utterlyidle.migrations.ModuleName;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.SqlDialect;

import static com.googlecode.utterlyidle.migrations.sql.SqlMigrations.sqlMigrationsInSamePackageAs;

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
