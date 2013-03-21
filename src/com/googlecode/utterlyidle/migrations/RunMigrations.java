package com.googlecode.utterlyidle.migrations;


import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.migrations.log.MigrationLogItem;
import com.googlecode.yadic.Resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.sequence;


public class RunMigrations implements Callable<Iterable<MigrationLogItem>> {
    private final Migrator migrator;
    private final ModuleMigrationsCollector moduleMigrationsCollector;
    private final Resolver resolver;

    public RunMigrations(Migrator migrator, ModuleMigrationsCollector moduleMigrationsCollector, Resolver resolver) {
        this.migrator = migrator;
        this.moduleMigrationsCollector = moduleMigrationsCollector;
        this.resolver = resolver;
    }

    public Iterable<MigrationLogItem> call() throws Exception {
        Sequence<ModuleMigrations> moduleMigrationsSequence = moduleMigrationsCollector.moduleMigrations(resolver);
        return moduleMigrationsSequence.fold(new ArrayList<MigrationLogItem>(), run());
    }

    private Callable2<List<MigrationLogItem>, ModuleMigrations, List<MigrationLogItem>> run() {
        return new Callable2<List<MigrationLogItem>, ModuleMigrations, List<MigrationLogItem>>() {
            public List<MigrationLogItem> call(List<MigrationLogItem> collector, ModuleMigrations moduleMigrations) throws Exception {
                Sequence<MigrationLogItem> logItemSequence = sequence(migrator.migrate(moduleMigrations.migrations(), moduleMigrations.moduleName()));
                for (MigrationLogItem migrationLogItem : logItemSequence) {
                    collector.add(migrationLogItem);
                }
                return collector;
            }
        };
    }
}
