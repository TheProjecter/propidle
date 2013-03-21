package com.googlecode.propidle.migrations.bootstrap.oracle;

import com.googlecode.propidle.migrations.bootstrap.Bootstrapper;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.yadic.Container;

public class OracleMigrationsModule implements Callable1<Container, Container> {
    public Container call(Container container) throws Exception {
        return container.add(Bootstrapper.class, OracleCreateMigrationLogTable.class);
    }
}