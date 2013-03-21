package com.googlecode.propidle.migrations.log;

import com.googlecode.propidle.migrations.MigrationNumber;
import com.googlecode.totallylazy.Option;
import com.googlecode.propidle.migrations.MigrationNumber;
import com.googlecode.propidle.migrations.ModuleName;

public interface MigrationLog {
    Option<MigrationLogItem> get(MigrationNumber migrationNumber, ModuleName moduleName);

    Iterable<MigrationLogItem> add(Iterable<MigrationLogItem> auditItems);

    Iterable<MigrationLogItem> list();
}
