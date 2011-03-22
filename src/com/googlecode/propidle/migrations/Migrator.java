package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.log.MigrationLogItem;

public interface Migrator {
    Iterable<MigrationLogItem> migrate(Iterable<Migration> migrations, final ModuleName moduleName);
}
