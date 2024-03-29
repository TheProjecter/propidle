package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.modules.MigrationActionsModule;
import com.googlecode.propidle.migrations.modules.MigrationQueriesModule;
import com.googlecode.propidle.server.PersistenceModules;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.time.Clock;
import com.googlecode.totallylazy.time.SystemClock;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Resolver;
import com.googlecode.yadic.SimpleContainer;
import com.googlecode.yadic.closeable.CloseableContainer;

import java.util.Properties;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.modules.Modules.activate;
import static com.googlecode.yadic.Containers.selfRegister;
import static com.googlecode.yadic.closeable.CloseableContainer.closeableContainer;

public class MigrationsContainer {
    public static CloseableContainer migrationsContainer(Resolver parentScope, Properties properties) throws Exception {
        Sequence<Module> moduleSequence = PersistenceModules.forMigrations(properties);

        CloseableContainer container = closeableContainer(selfRegister(new SimpleContainer(parentScope)));
        moduleSequence.forEach(activate(container, sequence(ApplicationScopedModule.class, RequestScopedModule.class)));

        new MigrationActionsModule().call(container);
        new MigrationQueriesModule().call(container);


        container.add(Clock.class, SystemClock.class);
        container.get(ModuleMigrationsCollector.class).add(PropIdleMigrations.class);
        return container;
    }
}