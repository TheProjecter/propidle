package com.googlecode.utterlyidle.migrations;

import com.googlecode.utterlyidle.migrations.log.MigrationLogItem;

public interface Migrator {
    Iterable<MigrationLogItem> migrate(Iterable<Migration> migrations, final ModuleName moduleName);
}
