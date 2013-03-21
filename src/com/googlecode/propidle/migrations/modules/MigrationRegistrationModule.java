package com.googlecode.propidle.migrations.modules;

import com.googlecode.propidle.migrations.ModuleMigrationsCollector;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

public class MigrationRegistrationModule implements RequestScopedModule{
    public Container addPerRequestObjects(Container container) {
        return container.add(ModuleMigrationsCollector.class);
    }
}
