package com.googlecode.propidle.migrations;

import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.propidle.status.StatusResource;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.migrations.RunMigrations;
import com.googlecode.utterlyidle.migrations.log.MigrationLogItem;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.yadic.Container;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Properties;

import static com.googlecode.propidle.server.PropertiesApplication.inTransaction;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.TimeCallable.calculateMilliseconds;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
import static com.googlecode.utterlyidle.proxy.Resource.urlOf;
import static com.googlecode.utterlyidle.rendering.Model.model;
import static java.lang.String.format;
import static java.lang.System.nanoTime;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path(MigrationResource.NAME)
@Produces(TEXT_HTML)
public class MigrationResource {
    private final Properties properties;
    public static final String NAME = "migrations";

    public MigrationResource(Properties properties) {
        this.properties = properties;
    }

    @POST
    public Model perform() throws Exception {
        long start = nanoTime();

        Container container = MigrationsContainer.migrationsContainer(properties);
        Sequence<MigrationLogItem> migrations;
        try {
            migrations = sequence(inTransaction(container, RunMigrations.class));
        } finally {
            container.close();
        }
        return model()
                .add("runTime", format("%s ms", calculateMilliseconds(start, nanoTime())))
                .add("statusReport", urlOf(resource(StatusResource.class).reportStatus()))
                .add("migrations", migrations.map(reportMigration()))
                .add(PropertiesModule.MODEL_NAME, NAME);
    }

    private Callable1<MigrationLogItem, String> reportMigration() {
        return new Callable1<MigrationLogItem, String>() {
            public String call(MigrationLogItem migrationLogItem) throws Exception {
                return format("%s-%s.sql", migrationLogItem.number(), migrationLogItem.name());
            }
        };
    }
}
