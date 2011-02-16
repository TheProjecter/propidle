package com.googlecode.propidle.server;

import com.googlecode.propidle.migrations.MigrationsModule;
import static com.googlecode.propidle.migrations.sql.AdminConnectionDetails.adminConnectionDetails;
import com.googlecode.propidle.migrations.history.MigrationEvent;
import com.googlecode.propidle.persistence.jdbc.ConnectionDetails;
import static com.googlecode.propidle.persistence.jdbc.ConnectionDetails.connectionDetails;
import com.googlecode.propidle.persistence.jdbc.SqlPersistenceModule;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.utterlyidle.simpleframework.RestServer;
import org.apache.lucene.store.RAMDirectory;

public class Server {
    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.valueOf(args[0]) : 8000;

        // Check port is free

        ConnectionDetails connection = connectionDetails("jdbc:hsqldb:file:propidledb", "SA", "");
        PropertiesApplication application = new PropertiesApplication(
                new RAMDirectory(),
                new SqlPersistenceModule(connection),
                new MigrationsModule(adminConnectionDetails(connection)));

        runMigrations(application);

        new RestServer(
                port,
                application);
        System.out.println("Running on port " + port);
    }

    private static Iterable<MigrationEvent> runMigrations(PropertiesApplication application) throws Exception {
        Sequence<MigrationEvent> migrations = sequence(application.inTransaction(RunMigrations.class));
        if (!migrations.isEmpty()) {
            System.out.println("Ran migrations:");
            System.out.println("--------------------------------------------");
            migrations.forEach(Runnables.<MigrationEvent>printLine(""));
            System.out.println("--------------------------------------------");
        }
        return migrations;
    }
}
