package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.log.MigrationLogItem;
import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.propidle.server.RunMigrations;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.yadic.Container;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Properties;

import static com.googlecode.propidle.server.PropertiesApplication.inTransaction;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.TimeCallable.calculateMilliseconds;
import static com.googlecode.utterlyidle.rendering.Model.model;
import static java.lang.String.format;
import static java.lang.System.nanoTime;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path(MigrationsResource.NAME)
@Produces(TEXT_HTML)
public class MigrationsResource {
    private final Properties properties;
    public static final String NAME = "migrations";

    public MigrationsResource(Properties properties) {
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
        Model model = model()
                .add("Run time", format("%s ms", calculateMilliseconds(start, nanoTime())))
                .add(PropertiesModule.MODEL_NAME, NAME);

        return migrations.fold(model, reportMigrations());
    }

    private Callable2<Model, MigrationLogItem, Model> reportMigrations() {
        return new Callable2<Model, MigrationLogItem, Model>() {
            public Model call(Model model, MigrationLogItem item) throws Exception {
                model.add(format("Migration %s", item.number()), format("ran at %s", item.dateRun()));
                return model;
            }
        };
    }
}
