package com.googlecode.propidle.server;

import com.googlecode.propidle.migrations.*;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import static com.googlecode.totallylazy.Sequences.sequence;

import java.util.concurrent.Callable;

public class RunMigrations implements Callable<Iterable<MigrationLogItem>> {
    private final Migrator migrator;
    private final Migrations migrations;
    private final ModuleName moduleName;

    public RunMigrations(Migrator migrator, Migrations migrations, ModuleName moduleName) {
        this.migrator = migrator;
        this.migrations = migrations;
        this.moduleName = moduleName;
    }

    public Iterable<MigrationLogItem> call() throws Exception {
        return migrator.migrate(migrations, moduleName);
    }
}
