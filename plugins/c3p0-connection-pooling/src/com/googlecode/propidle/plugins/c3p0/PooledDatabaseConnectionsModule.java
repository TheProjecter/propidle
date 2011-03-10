package com.googlecode.propidle.plugins.c3p0;

import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

import javax.sql.DataSource;
import java.sql.Connection;

public class PooledDatabaseConnectionsModule implements RequestScopedModule, ApplicationScopedModule{

    public Module addPerRequestObjects(Container container) {
        container.remove(Connection.class);
        container.addActivator(Connection.class, DataSourceConnectionActivator.class);
        return this;
    }

    public Module addPerApplicationObjects(Container container) {
        container.addActivator(DataSource.class, C3P0DataSourceActivator.class);
        return this;
    }
}
