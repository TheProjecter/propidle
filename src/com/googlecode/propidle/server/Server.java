package com.googlecode.propidle.server;

import static com.googlecode.propidle.client.loaders.PropertiesAtUrl.propertiesAtUrl;
import com.googlecode.propidle.migrations.MigrationsModule;
import com.googlecode.propidle.migrations.history.MigrationEvent;
import com.googlecode.propidle.persistence.jdbc.SqlPersistenceModule;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.TimeCallable.calculateMilliseconds;
import com.googlecode.utterlyidle.io.Url;
import static com.googlecode.utterlyidle.io.Url.url;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.simpleframework.RestServer;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.System.nanoTime;
import java.util.Properties;
import java.util.concurrent.Callable;

public class Server {
    public static final String PORT = "port";
    private static RestServer server;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("please provide a valid url for a properties file");
            return;
        }

        new Server(url(args[0]));
    }

    public Server(Url propertiesUrl, Module... extraModules) throws Exception {
        this(propertiesAtUrl(propertiesUrl.toURL()), extraModules);
    }

    public Server(Properties properties, Module... extraModules) throws Exception {
        this(returns(properties), extraModules);
    }

    public Server(Callable<Properties> propertyLoader, Module... extraModules) throws Exception {
        PropertiesApplication application = new PropertiesApplication(
                propertyLoader,
                new RAMDirectory(),
                new SqlPersistenceModule(),
                modules(new MigrationsModule(), extraModules));

        int port = parseInt(propertyLoader.call().getProperty(PORT));

        runMigrations(application);
        rebuildLuceneIndexes(application);
        startServer(port, application);

    }

    public void stop() throws Exception {
        server.stop();
    }

    private Module[] modules(final Module module, Module... extraModules) {
        return sequence(module).join(sequence(extraModules)).toArray(Module.class);
    }

    private static void startServer(int port, PropertiesApplication application) throws IOException {
        long start = nanoTime();
        server = new RestServer(
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
