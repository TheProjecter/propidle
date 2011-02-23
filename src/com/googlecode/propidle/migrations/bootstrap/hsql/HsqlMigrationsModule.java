package com.googlecode.propidle.migrations.bootstrap.hsql;

import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import com.googlecode.propidle.migrations.bootstrap.Bootstrapper;
import com.googlecode.totallylazy.Callable1;

public class HsqlMigrationsModule implements Callable1<Container,Container> {
    public Container call(Container container) {
        return container.add(Bootstrapper.class, HsqlCreateMigrationLogTable.class);
    }
}
