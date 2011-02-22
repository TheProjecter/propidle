package com.googlecode.propidle.util;

import com.googlecode.propidle.migrations.MigrationsModule;
import com.googlecode.propidle.persistence.jdbc.SqlPersistenceModule;
import com.googlecode.propidle.persistence.jdbc.MigrationConnectionDetails;
import com.googlecode.propidle.persistence.jdbc.ConnectionDetails;
import static com.googlecode.propidle.server.PropertiesApplication.inTransaction;
import com.googlecode.propidle.server.RunMigrations;
import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.util.time.SystemClock;
import static com.googlecode.propidle.properties.Properties.properties;
import com.googlecode.totallylazy.records.Records;
import static com.googlecode.totallylazy.Pair.pair;
import com.googlecode.yadic.SimpleContainer;

import static java.util.UUID.randomUUID;
import java.util.Properties;

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

        new SqlPersistenceModule().addPerRequestObjects(container);

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
