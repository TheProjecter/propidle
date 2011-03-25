package com.googlecode.propidle.util;

import com.googlecode.propidle.PersistenceMechanism;
import com.googlecode.propidle.server.Server;
import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.util.time.SystemClock;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.utterlyidle.migrations.RunMigrations;
import com.googlecode.utterlyidle.migrations.log.MigrationLogItem;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;

import java.util.Properties;

import static com.googlecode.propidle.PersistenceMechanism.HSQL;
import static com.googlecode.propidle.PersistenceMechanism.IN_MEMORY;
import static com.googlecode.propidle.PersistenceMechanism.PERSISTENCE;
import static com.googlecode.propidle.migrations.MigrationsContainer.migrationsContainer;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.server.PersistenceModules.persistenceModules;
import static com.googlecode.propidle.server.PropertiesApplication.inTransaction;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.utterlyidle.modules.Modules.addPerApplicationObjects;
import static com.googlecode.utterlyidle.modules.Modules.addPerRequestObjects;
import static java.util.UUID.randomUUID;

public class TestRecords {

    public static Records testRecordsWithAllMigrationsRun() {
        Properties properties = hsqlConfiguration();
        runMigrations(properties);
        Container container = container(properties);
        return container.get(Records.class);
    }

    public static void runMigrations(Properties properties) {
        try {
            Iterable<MigrationLogItem> logItemIterable = inTransaction(migrationsContainer(properties), RunMigrations.class);
            System.out.println("logItemIterable = " + logItemIterable);
        } catch (Exception e) {
            throw new RuntimeException("Problem running migrations", e);
        }
    }

    public static Properties hsqlConfiguration() {
        return properties(
                pair(PERSISTENCE, HSQL.name()),

                pair(Server.JDBC_URL, "jdbc:hsqldb:mem:" + randomUUID()),
                pair(Server.JDBC_USER, "SA"),
                pair(Server.JDBC_PASSWORD, ""),

                pair(Server.MIGRATION_JDBC_USER, "SA"),
                pair(Server.MIGRATION_JDBC_PASSWORD, "")
        );
    }

    private static Container container(final Properties properties) {
        SimpleContainer container = new SimpleContainer();
        container.add(Clock.class, SystemClock.class);
        container.addInstance(Properties.class, properties);
        return addToContainer(container, persistenceModules(properties));
    }

    private static Container addToContainer(Container container, Sequence<Module> modules) {
        modules.safeCast(ApplicationScopedModule.class).forEach(addPerApplicationObjects(container));
        modules.safeCast(RequestScopedModule.class).forEach(addPerRequestObjects(container));
        return container;
    }
}
