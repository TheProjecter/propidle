package com.googlecode.propidle.migrations.modules;

import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.propidle.migrations.log.MigrationLogFromRecords;
import com.googlecode.totallylazy.UnaryFunction;
import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.propidle.migrations.log.MigrationLogFromRecords;
import com.googlecode.yadic.Container;

public class MigrationQueriesModule extends UnaryFunction<Container> {
    public Container call(Container container) throws Exception {
        return container.
                add(MigrationLog.class, MigrationLogFromRecords.class);
    }

    public static MigrationQueriesModule migrationQueriesModule() {
        return new MigrationQueriesModule();
    }
}
