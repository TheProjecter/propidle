package com.googlecode.propidle.server;

import com.googlecode.propidle.migrations.MigrationsModule;
import com.googlecode.propidle.migrations.history.MigrationEvent;
import static com.googlecode.propidle.migrations.sql.AdminConnectionDetails.adminConnectionDetails;
import com.googlecode.propidle.persistence.jdbc.ConnectionDetails;
import static com.googlecode.propidle.persistence.jdbc.ConnectionDetails.connectionDetails;
import com.googlecode.propidle.persistence.jdbc.SqlPersistenceModule;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.TimeCallable.calculateMilliseconds;
import com.googlecode.utterlyidle.simpleframework.RestServer;
import org.apache.lucene.store.RAMDirectory;

import static java.lang.String.format;
import static java.lang.System.nanoTime;
import java.io.IOException;

public class Server {
    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.valueOf(args[0]) : 8000;

        // Check port is free

        ConnectionDetails connection = connectionDetails("jdbc:hsqldb:file:test", "SA", "");
        PropertiesApplication application = new PropertiesApplication(
                new RAMDirectory(),
                new SqlPersistenceModule(connection),
                new MigrationsModule(adminConnectionDetails(connection)));

        runMigrations(application);
        rebuildLuceneIndexes(application);
        startServer(port, application);
    }

    private static void startServer(int port, PropertiesApplication application) throws IOException {
        long start = nanoTime();
        new RestServer(
                port,
                application);
        System.out.println(format("Started server in %sms", calculateMilliseconds(start, nanoTime())));
        System.out.println(format("Running on port %s", port));
    }

    private static void rebuildLuceneIndexes(PropertiesApplication application) throws Exception {
        System.out.println("Re-indexing...");
        long start = nanoTime();
        application.inTransaction(RebuildIndex.class);
        System.out.println(format("Re-indexing finished in %sms", calculateMilliseconds(start, nanoTime())));
    }

    private static Iterable<MigrationEvent> runMigrations(PropertiesApplication application) throws Exception {
        System.out.println("Running migrations...");
        long start = nanoTime();
        Sequence<MigrationEvent> migrations = sequence(application.inTransaction(RunMigrations.class));
        if (!migrations.isEmpty()) {
            System.out.println(format("Ran migrations in %sms", calculateMilliseconds(start, nanoTime())));
            System.out.println("--------------------------------------------");
            migrations.forEach(Runnables.<MigrationEvent>printLine("%s"));
            System.out.println("--------------------------------------------");
        } else {
            System.out.println("No migrations to run");
        }
        return migrations;
    }
}
