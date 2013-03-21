package com.googlecode.utterlyidle.migrations.modules;

import com.googlecode.totallylazy.UnaryFunction;
import com.googlecode.utterlyidle.migrations.LockMigrationLogRecord;
import com.googlecode.utterlyidle.migrations.MigrationLogCheckingMigrator;
import com.googlecode.utterlyidle.migrations.Migrator;
import com.googlecode.utterlyidle.migrations.bootstrap.MigrationLogBootstrapper;
import com.googlecode.yadic.Container;

public class MigrationActionsModule extends UnaryFunction<Container> {
    public Container call(Container container) throws Exception {
        return container.
                add(Migrator.class, MigrationLogCheckingMigrator.class).
                decorate(Migrator.class, LockMigrationLogRecord.class).
                decorate(Migrator.class, MigrationLogBootstrapper.class);
    }
}
