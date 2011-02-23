package com.googlecode.propidle.util;

import static com.googlecode.propidle.MigrationsModules.migrationsModules;
import static com.googlecode.propidle.PersistenceMechanism.HSQL;
import static com.googlecode.propidle.PersistenceMechanism.PERSISTENCE;
import static com.googlecode.propidle.persistence.PersistenceModules.persistenceModules;
import com.googlecode.propidle.persistence.jdbc.ConnectionDetails;
import com.googlecode.propidle.persistence.jdbc.MigrationConnectionDetails;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.server.PropertiesApplication.inTransaction;
import com.googlecode.propidle.server.RunMigrations;
import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.util.time.SystemClock;
import static com.googlecode.totallylazy.Pair.pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import static com.googlecode.utterlyidle.modules.Modules.addPerApplicationObjects;
import static com.googlecode.utterlyidle.modules.Modules.addPerRequestObjects;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;

import java.util.Properties;
import static java.util.UUID.randomUUID;

public class TestRecords {
    public static Records emptyTestRecords() {
        return container(hsqlConfiguraton()).get(Records.class);
    }

    public static Records testRecordsWithAllMigrationsRun() {
        Properties properties = hsqlConfiguraton();
        Container container = addToContainer(container(properties), migrationsModules(properties));
        try {
            inTransaction(container, RunMigrations.class);
        } catch (Exception e) {
            throw new RuntimeException("Problem running migrations", e);
        }
        return container.get(Records.class);
    }

    public static Properties hsqlConfiguraton() {
        String jdbcUrl = "jdbc:hsqldb:mem:" + randomUUID();
        return properties(
                pair(PERSISTENCE, HSQL.name()),

                pair(ConnectionDetails.URL, jdbcUrl),
                pair(ConnectionDetails.USER, "SA"),
                pair(ConnectionDetails.PASSWORD, ""),

                pair(MigrationConnectionDetails.USER, "SA"),
                pair(MigrationConnectionDetails.PASSWORD, "")
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
