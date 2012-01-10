package com.googlecode.propidle.migrations;

import com.googlecode.propidle.status.StatusResource;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.Redirector;
import com.googlecode.utterlyidle.annotations.POST;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;
import com.googlecode.utterlyidle.migrations.RunMigrations;
import com.googlecode.utterlyidle.migrations.log.MigrationLogItem;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.Resolver;

import java.util.Properties;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.propidle.server.PropertiesApplication.inTransaction;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.TimeCallable.calculateMilliseconds;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static java.lang.String.format;
import static java.lang.System.nanoTime;

@Path(MigrationResource.NAME)
@Produces(TEXT_HTML)
public class MigrationResource {
    private final Properties properties;
    private final Resolver myScope;
    private final Redirector redirector;
    public static final String NAME = "migrations";

    public MigrationResource(Properties properties, Resolver myScope, Redirector redirector) {
        this.properties = properties;
        this.myScope = myScope;
        this.redirector = redirector;
    }

    @POST
    public Model perform() throws Exception {
        long start = nanoTime();

        Container container = MigrationsContainer.migrationsContainer(myScope, properties);
        Sequence<MigrationLogItem> migrations;
        try {
            migrations = sequence(inTransaction(container, RunMigrations.class));
        } finally {
            container.close();
        }
        return modelWithName(NAME)
                .add("runTime", format("%s ms", calculateMilliseconds(start, nanoTime())))
                .add("statusReport", redirector.resourceUriOf(method(on(StatusResource.class).reportStatus())))
                .add("migrations", migrations.map(reportMigration()));
    }

    private Callable1<MigrationLogItem, String> reportMigration() {
        return new Callable1<MigrationLogItem, String>() {
            public String call(MigrationLogItem migrationLogItem) throws Exception {
                return format("%s-%s.sql", migrationLogItem.number(), migrationLogItem.name());
            }
        };
    }
}
