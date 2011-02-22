package com.googlecode.propidle.util;

import com.googlecode.propidle.migrations.MigrationsModule;
import static com.googlecode.propidle.persistence.PropertiesBasedPersistence.Option.IN_MEMORY;
import static com.googlecode.propidle.persistence.PropertiesBasedPersistence.persistenceStrategy;
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
import com.googlecode.yadic.SimpleContainer;

import java.util.Properties;
import static java.util.UUID.randomUUID;

public class TestRecords {
    public static Records emptyTestRecords() {
        return container().get(Records.class);
    }

    public static Records testRecordsWithAllMigrationsRun() {
        SimpleContainer container = container();

        MigrationsModule migrationsModule = new MigrationsModule();
        migrationsModule.addPerApplicationObjects(container);
        migrationsModule.addPerRequestObjects(container);

        try {
            inTransaction(container, RunMigrations.class);
        } catch (Exception e) {
            throw new RuntimeException("Problem running migrations", e);
        }
        return container.get(Records.class);
    }

    private static SimpleContainer container() {
        SimpleContainer container = new SimpleContainer();
        container.add(Clock.class, SystemClock.class);
        container.addInstance(Properties.class, inMemoryDatabaseConfiguraton());

        Sequence<Module> modules = persistenceStrategy(IN_MEMORY);
        modules.safeCast(RequestScopedModule.class).forEach(addPerRequestObjects(container));
        modules.safeCast(ApplicationScopedModule.class).forEach(addPerApplicationObjects(container));

        return container;
    }

    public static Properties inMemoryDatabaseConfiguraton() {
        String jdbcUrl = "jdbc:hsqldb:mem:" + randomUUID();
        return properties(
                pair(ConnectionDetails.URL, jdbcUrl),
                pair(ConnectionDetails.USER, "SA"),
                pair(ConnectionDetails.PASSWORD, ""),

                pair(MigrationConnectionDetails.USER, "SA"),
                pair(MigrationConnectionDetails.PASSWORD, "")
        );
    }
}
