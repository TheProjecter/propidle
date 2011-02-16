package com.googlecode.propidle.server;

import com.googlecode.propidle.migrations.*;
import com.googlecode.propidle.migrations.history.MigrationEvent;
import static com.googlecode.totallylazy.Sequences.sequence;

import java.util.concurrent.Callable;

public class RunMigrations implements Callable<Iterable<MigrationEvent>> {
    private final Migrator migrator;
    private final Migrations migrations;

    public RunMigrations(Migrator migrator, Migrations migrations) {
        this.migrator = migrator;
        this.migrations = migrations;
    }

    public Iterable<MigrationEvent> call() throws Exception {
        return migrator.migrate(migrations);
    }
}
