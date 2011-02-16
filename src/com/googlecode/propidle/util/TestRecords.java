package com.googlecode.propidle.util;

import com.googlecode.propidle.migrations.MigrationsModule;
import com.googlecode.propidle.persistence.jdbc.ConnectionDetails;
import static com.googlecode.propidle.persistence.jdbc.ConnectionDetails.connectionDetails;
import com.googlecode.propidle.persistence.jdbc.SqlPersistenceModule;
import static com.googlecode.propidle.server.PropertiesApplication.inTransaction;
import com.googlecode.propidle.server.RunMigrations;
import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.util.time.SystemClock;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.yadic.SimpleContainer;

import static java.util.UUID.randomUUID;

public class TestRecords {
    public static Records emptyTestRecords() {
        return container().get(Records.class);
    }

    public static Records testRecordsWithAllMigrationsRun() {
        SimpleContainer container = container();

        new MigrationsModule(container.get(ConnectionDetails.class)).
                addPerRequestObjects(container);
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

        ConnectionDetails connection = connectionDetails("jdbc:hsqldb:mem:" + randomUUID(), "SA", "");

        new SqlPersistenceModule(connection).
                addPerRequestObjects(container);
        return container;
    }


}
