package com.googlecode.propidle.migrations;

import com.googlecode.propidle.server.PersistenceModules;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.migrations.MigrationQueriesModule;
import com.googlecode.utterlyidle.migrations.MigrationsModule;
import com.googlecode.utterlyidle.migrations.ModuleMigrationsCollector;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.Resolver;
import com.googlecode.yadic.SimpleContainer;

import java.util.Properties;

import static com.googlecode.propidle.MigrationsModules.migrationsModules;
import static com.googlecode.propidle.util.Callables.chain;
import static com.googlecode.propidle.util.Modules.adaptUtterlyIdleModule;

public class MigrationsContainer {
    public static Container migrationsContainer(Properties properties) throws Exception {
        Sequence<Module> moduleSequence = PersistenceModules.forMigrations(properties);
        Sequence<Callable1<Container, Container>> persistenceModules = moduleSequence.map(adaptUtterlyIdleModule());
        Container container = migrationsModules(properties).join(persistenceModules).fold(new SimpleContainer(), chain(Container.class));
        new MigrationsModule().call(container);
        new MigrationQueriesModule().call(container);
        container.addInstance(Resolver.class, container);


        container.add(com.googlecode.utterlyidle.migrations.util.time.Clock.class, com.googlecode.utterlyidle.migrations.util.time.SystemClock.class);
        container.get(ModuleMigrationsCollector.class).add(PropIdleMigrations.class);
        return container;
    }
}