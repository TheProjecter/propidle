package com.googlecode.propidle.server;

import static com.googlecode.propidle.MigrationsModules.migrationsModules;
import static com.googlecode.propidle.client.loaders.PropertiesAtUrl.propertiesAtUrl;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import static com.googlecode.propidle.server.PersistenceModules.persistenceModules;
import static com.googlecode.propidle.server.PropertiesApplication.inTransaction;
import static com.googlecode.propidle.util.Callables.chain;
import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.util.time.SystemClock;
import com.googlecode.totallylazy.Callable1;
import static com.googlecode.totallylazy.Callables.returns;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.TimeCallable.calculateMilliseconds;
import com.googlecode.utterlyidle.io.Url;
import static com.googlecode.utterlyidle.io.Url.url;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import static com.googlecode.utterlyidle.modules.Modules.addPerApplicationObjects;
import static com.googlecode.utterlyidle.modules.Modules.addPerRequestObjects;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.simpleframework.RestServer;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.System.nanoTime;
import java.util.Properties;
import java.util.concurrent.Callable;

public class Server {
    public static final String PORT = "port";
    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USER = "jdbc.user";
    public static final String JDBC_PASSWORD = "jdbc.password";
    public static final String MIGRATION_JDBC_USER = "migration.jdbc.user";
    public static final String MIGRATION_JDBC_PASSWORD = "migration.jdbc.password";

    private static RestServer server;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("First argument should be a valid url for a properties file");
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
        Properties properties = propertyLoader.call();
        PropertiesApplication application = new PropertiesApplication(
                propertyLoader,
                new RAMDirectory(),
                persistenceModules(properties).join(extraModules));

        int port = parseInt(propertyLoader.call().getProperty(PORT));

        runMigrations(properties);
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

    private static Iterable<MigrationLogItem> runMigrations(Properties properties) throws Exception {
        System.out.println("Running migrations...");
        long start = nanoTime();
        Sequence<Callable1<Container, Container>> persistenceModules = PersistenceModules.forMigrations(properties).map(adaptUtterlyIdleModule());
        Container container = migrationsModules(properties).join(persistenceModules).fold(new SimpleContainer(), chain(Container.class));
        container.add(Clock.class, SystemClock.class);

        Sequence<MigrationLogItem> migrations;
        try {
            migrations = sequence(inTransaction(container, RunMigrations.class));
        } finally {
            container.close();
        }

        reportMigrations(migrations, calculateMilliseconds(start, nanoTime()));
        return migrations;
    }

    private static void reportMigrations(Sequence<MigrationLogItem> migrations, double timeTaken) {
        if (!migrations.isEmpty()) {
            System.out.println(format("Ran migrations in %sms", timeTaken));
            System.out.println("--------------------------------------------");
            migrations.forEach(Runnables.<MigrationLogItem>printLine("%s"));
            System.out.println("--------------------------------------------");
        } else {
            System.out.println("No migrations to run");
        }
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
