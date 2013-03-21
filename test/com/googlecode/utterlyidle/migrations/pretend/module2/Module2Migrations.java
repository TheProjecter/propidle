package com.googlecode.utterlyidle.migrations.pretend.module2;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.utterlyidle.migrations.Migrations;
import com.googlecode.utterlyidle.migrations.ModuleMigrations;
import com.googlecode.utterlyidle.migrations.ModuleName;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.SqlDialect;

import static com.googlecode.utterlyidle.migrations.sql.SqlMigrations.sqlMigrationsInSamePackageAs;

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
