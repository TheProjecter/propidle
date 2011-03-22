package com.googlecode.propidle.migrations;

import com.googlecode.propidle.server.PersistenceModules;
import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.util.time.SystemClock;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;

import java.util.Properties;

import static com.googlecode.propidle.MigrationsModules.migrationsModules;
import static com.googlecode.propidle.migrations.ModuleName.moduleName;
import static com.googlecode.propidle.util.Callables.chain;
import static com.googlecode.propidle.util.Modules.adaptUtterlyIdleModule;

public class MigrationsContainer {
    public static Container migrationsContainer(Properties properties) {
        Sequence<Module> moduleSequence = PersistenceModules.forMigrations(properties);
        Sequence<Callable1<Container, Container>> persistenceModules = moduleSequence.map(adaptUtterlyIdleModule());
        Container container = migrationsModules(properties).join(persistenceModules).fold(new SimpleContainer(), chain(Container.class));
        container.add(Clock.class, SystemClock.class);
        container.addInstance(ModuleName.class, moduleName("Propidle-Core"));
        return container;
    }
}