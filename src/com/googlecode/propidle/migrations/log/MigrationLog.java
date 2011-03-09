package com.googlecode.propidle.migrations.log;

import com.googlecode.totallylazy.Option;
import com.googlecode.propidle.migrations.MigrationNumber;

public interface MigrationLog {
    Option<MigrationLogItem> get(MigrationNumber migrationNumber);

    Iterable<MigrationLogItem> add(Iterable<MigrationLogItem> auditItems);

    Iterable<MigrationLogItem> list();
}
