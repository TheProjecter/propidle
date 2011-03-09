package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.log.MigrationLogFromRecords;
import com.googlecode.propidle.server.PersistenceModules;
import com.googlecode.propidle.server.Server;
import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.util.time.SystemClock;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;

import java.util.Properties;

import static com.googlecode.propidle.MigrationsModules.migrationsModules;
import static com.googlecode.propidle.util.Callables.chain;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.modules.Modules.addPerApplicationObjects;
import static com.googlecode.utterlyidle.modules.Modules.addPerRequestObjects;

public class MigrationsContainer {
    public static Container migrationsContainer(Properties properties) {
        Sequence<Callable1<Container, Container>> persistenceModules = PersistenceModules.forMigrations(properties).map(adaptUtterlyIdleModule());
        Container container = migrationsModules(properties).join(persistenceModules).fold(new SimpleContainer(), chain(Container.class));
        container.add(Clock.class, SystemClock.class);
        container.add(PropIdleMigrations.class);
        container.add(MigrationLogFromRecords.class);
        return container;
    }

    private static Callable1<? super Module, Callable1<Container, Container>> adaptUtterlyIdleModule() {
        return new Callable1<Module, Callable1<Container, Container>>() {
            public Callable1<Container, Container> call(final Module module) throws Exception {
                return new Callable1<Container, Container>() {
                    public Container call(Container container) throws Exception {
                        sequence(module).safeCast(ApplicationScopedModule.class).forEach(addPerApplicationObjects(container));
                        sequence(module).safeCast(RequestScopedModule.class).forEach(addPerRequestObjects(container));
                        return container;
                    }
                };
            }
        };
    }

}
