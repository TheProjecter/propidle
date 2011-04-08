package com.googlecode.propidle.server;

import static com.googlecode.propidle.client.loaders.PropertiesAtUrl.propertiesAtUrl;

import static com.googlecode.propidle.server.PersistenceModules.persistenceModules;
import com.googlecode.totallylazy.*;

import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.callables.TimeCallable.calculateMilliseconds;

import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.io.Url;

import static com.googlecode.utterlyidle.io.Url.url;

import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.simpleframework.RestServer;
import org.apache.lucene.store.RAMDirectory;

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

        Integer schemaVersion = schemaVersion(application.inTransaction(ReportSchemaVersion.class));
        System.out.println(format("Running with database schema version %s", schemaVersion));
        if (schemaVersion > 0) {
            rebuildLuceneIndexes(application);
        }
        startServer(port, application);

    }

    private Integer schemaVersion(Either<Throwable, Integer> schemaVersion) {
        if (schemaVersion.isRight()) {
            return schemaVersion.right();
        }
        return 0;
    }

    public void stop() throws Exception {
        server.stop();
    }

    private static void startServer(int port, PropertiesApplication application) throws Exception {
        long start = nanoTime();
        server = new RestServer(port, BasePath.basePath("/"), application);

        application.call(RegisterCountingMBeans.class);

        System.out.println(format("Started server in %sms", calculateMilliseconds(start, nanoTime())));
        System.out.println(format("Running on port %s", port));
    }

    private static void rebuildLuceneIndexes(PropertiesApplication application) throws Exception {
        System.out.println("Re-indexing...");
        long start = nanoTime();
        application.inTransaction(RebuildIndex.class);
        System.out.println(format("Re-indexing finished in %sms", calculateMilliseconds(start, nanoTime())));
    }
}
