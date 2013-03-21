package com.googlecode.utterlyidle.migrations.log;

import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.migrations.MigrationNumber;
import com.googlecode.utterlyidle.migrations.ModuleName;

public interface MigrationLog {
    Option<MigrationLogItem> get(MigrationNumber migrationNumber, ModuleName moduleName);

    Iterable<MigrationLogItem> add(Iterable<MigrationLogItem> auditItems);

    Iterable<MigrationLogItem> list();
}
