package com.googlecode.propidle.migrations;

import com.googlecode.propidle.server.PersistenceModules;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.migrations.ModuleMigrationsCollector;
import com.googlecode.utterlyidle.migrations.modules.MigrationActionsModule;
import com.googlecode.utterlyidle.migrations.modules.MigrationQueriesModule;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.Resolver;
import com.googlecode.yadic.SimpleContainer;

import java.util.Properties;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.modules.Modules.activate;
import static com.googlecode.yadic.Containers.selfRegister;

public class MigrationsContainer {
    public static Container migrationsContainer(Resolver parentScope, Properties properties) throws Exception {
        Sequence<Module> moduleSequence = PersistenceModules.forMigrations(properties);
        
        Container container = selfRegister(new SimpleContainer(parentScope));
        moduleSequence.forEach(activate(container, sequence(ApplicationScopedModule.class, RequestScopedModule.class)));

        new MigrationActionsModule().call(container);
        new MigrationQueriesModule().call(container);


        container.add(com.googlecode.utterlyidle.migrations.util.time.Clock.class, com.googlecode.utterlyidle.migrations.util.time.SystemClock.class);
        container.get(ModuleMigrationsCollector.class).add(PropIdleMigrations.class);
        return container;
    }
}