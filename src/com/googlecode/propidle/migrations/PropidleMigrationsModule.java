package com.googlecode.propidle.migrations;

import com.googlecode.utterlyidle.migrations.ModuleMigrationsCollector;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

public class PropidleMigrationsModule implements RequestScopedModule {
    public Module addPerRequestObjects(Container container) {
        container.add(ModuleMigrationsCollector.class);
        container.get(ModuleMigrationsCollector.class).add(PropIdleMigrations.class);
        return this;
    }
}
