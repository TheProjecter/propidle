package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.propidle.migrations.log.MigrationLogFromRecords;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.yadic.Container;

public class MigrationQueriesModule implements Callable1<Container, Container> {
    public Container call(Container container) throws Exception {
        return container.
                add(MigrationLog.class, MigrationLogFromRecords.class).
                add(Migrations.class, PropIdleMigrations.class);
    }

    public static MigrationQueriesModule migrationQueriesModule() {
        return new MigrationQueriesModule();
    }
}
