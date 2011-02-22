package com.googlecode.propidle.server;

import static com.googlecode.propidle.client.loaders.PropertiesAtUrl.propertiesAtUrl;
import com.googlecode.propidle.migrations.MigrationsModule;
import com.googlecode.propidle.migrations.history.MigrationEvent;
import static com.googlecode.propidle.persistence.PropertiesBasedPersistence.persistenceStrategy;
import static com.googlecode.totallylazy.Callables.returns;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
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

    public Server(Url propertiesUrl) throws Exception {
        this(propertiesUrl, Sequences.<Module>sequence());
    }

    public Server(Url propertiesUrl, Iterable<Module> extraModules) throws Exception {
        this(propertiesAtUrl(propertiesUrl.toURL()), extraModules);
    }

    public Server(Properties properties, Iterable<Module> extraModules) throws Exception {
        this(returns(properties), extraModules);
    }

    public Server(Callable<Properties> propertyLoader, Iterable<Module> extraModules) throws Exception {
        PropertiesApplication application = new PropertiesApplication(
                propertyLoader,
                new RAMDirectory(),
                persistenceStrategy(propertyLoader.call()).
                        join(extraModules).
                        add(new MigrationsModule()));

        int port = parseInt(propertyLoader.call().getProperty(PORT));

        runMigrations(application);
        rebuildLuceneIndexes(application);
        startServer(port, application);

    }

    public void stop() throws Exception {
        server.stop();
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
