package com.googlecode.propidle.migrations.bootstrap.hsql;

import com.googlecode.totallylazy.UnaryFunction;
import com.googlecode.propidle.migrations.bootstrap.Bootstrapper;
import com.googlecode.yadic.Container;

public class HsqlMigrationsModule extends UnaryFunction<Container> {
    public Container call(Container container) {
        return container.add(Bootstrapper.class, HsqlCreateMigrationLogTable.class);
    }
}
