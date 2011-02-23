package com.googlecode.propidle.migrations.bootstrap.hsql;

import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import com.googlecode.propidle.migrations.bootstrap.Bootstrapper;

public class HsqlMigrationsModule implements RequestScopedModule{
    public Module addPerRequestObjects(Container container) {
        container.add(Bootstrapper.class, HsqlCreateMigrationLogTable.class);
        return this;
    }
}
