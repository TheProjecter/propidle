package com.googlecode.propidle.migrations.bootstrap.oracle;

import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import com.googlecode.propidle.migrations.bootstrap.Bootstrapper;
import com.googlecode.propidle.migrations.bootstrap.hsql.HsqlCreateMigrationLogTable;

public class OracleMigrationsModule implements RequestScopedModule{
    public Module addPerRequestObjects(Container container) {
        container.add(Bootstrapper.class, OracleCreateMigrationLogTable.class);
        return this;
    }
}