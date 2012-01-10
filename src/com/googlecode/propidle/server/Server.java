package com.googlecode.propidle.server;

import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.ServerActivator;
import com.googlecode.utterlyidle.ServerConfiguration;
import com.googlecode.utterlyidle.modules.Module;
import org.apache.lucene.store.RAMDirectory;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.client.loaders.PropertiesAtUrl.propertiesAtUrl;
import static com.googlecode.propidle.server.PersistenceModules.persistenceModules;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Uri.uri;
import static java.lang.String.format;

public class Server {
    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USER = "jdbc.user";
    public static final String JDBC_PASSWORD = "jdbc.password";
    public static final String MIGRATION_JDBC_USER = "migration.jdbc.user";
    public static final String MIGRATION_JDBC_PASSWORD = "migration.jdbc.password";

    private static com.googlecode.utterlyidle.Server server;
    private PropertiesApplication application;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("First argument should be a valid url for a properties file");
            return;
        }

        new Server(uri(args[0]));
    }

    public Server(Uri propertiesUrl) throws Exception {
        this(propertiesUrl, empty(Module.class));
    }

    public Server(Uri propertiesUrl, Iterable<Module> extraModules) throws Exception {
        this(propertiesAtUrl(propertiesUrl.toURL()), extraModules);
    }

    public Server(Properties properties, Iterable<Module> extraModules) throws Exception {
        this(returns(properties), extraModules);
    }

    public Server(Callable<Properties> propertyLoader, Iterable<Module> extraModules) throws Exception {
        Properties properties = propertyLoader.call();
        final ServerConfiguration serverConfig = new ServerConfiguration(properties);
        application = new PropertiesApplication(
                propertyLoader,
                new RAMDirectory(),
                persistenceModules(properties).join(extraModules), serverConfig.basePath());

        Integer schemaVersion = schemaVersion(application.inTransaction(ReportSchemaVersion.class));
        System.out.println(format("Running with database schema version %s", schemaVersion));
        startServer(application, serverConfig);

    }

    private Integer schemaVersion(Either<Throwable, Integer> schemaVersion) {
        if (schemaVersion.isRight()) {
            return schemaVersion.right();
        }
        return 0;
    }

    public void stop() throws Exception {
        server.close();
        application.close();
    }

    private static void startServer(final PropertiesApplication application, final ServerConfiguration serverConfig) throws Exception {
        server = call(new ServerActivator(application, serverConfig));
        application.applicationScope().get(RegisterCountingMBeans.class).call();
        try {
            application.startPropertyDependentTasks();
        } catch (Exception e) {
            application.close();
            server.close();
            throw new RuntimeException(e);
        }
    }
}
