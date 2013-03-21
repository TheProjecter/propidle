package com.googlecode.propidle.migrations.modules;

import com.googlecode.propidle.migrations.LockMigrationLogRecord;
import com.googlecode.propidle.migrations.MigrationLogCheckingMigrator;
import com.googlecode.propidle.migrations.Migrator;
import com.googlecode.totallylazy.UnaryFunction;
import com.googlecode.propidle.migrations.LockMigrationLogRecord;
import com.googlecode.propidle.migrations.MigrationLogCheckingMigrator;
import com.googlecode.propidle.migrations.Migrator;
import com.googlecode.propidle.migrations.bootstrap.MigrationLogBootstrapper;
import com.googlecode.yadic.Container;

public class MigrationActionsModule extends UnaryFunction<Container> {
    public Container call(Container container) throws Exception {
        return container.
                add(Migrator.class, MigrationLogCheckingMigrator.class).
                decorate(Migrator.class, LockMigrationLogRecord.class).
                decorate(Migrator.class, MigrationLogBootstrapper.class);
    }
}
