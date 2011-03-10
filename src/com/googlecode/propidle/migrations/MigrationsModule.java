package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.bootstrap.MigrationLogBootstrapper;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.yadic.Container;

public class MigrationsModule implements Callable1<Container, Container> {
    public Container call(Container container) throws Exception {
        return container.
                add(Migrator.class, MigrationLogCheckingMigrator.class).
                decorate(Migrator.class, LockMigrationLogRecord.class).
                decorate(Migrator.class, MigrationLogBootstrapper.class);
    }
}
