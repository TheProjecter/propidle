package com.googlecode.propidle.migrations.util;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.UnaryFunction;
import com.googlecode.propidle.migrations.modules.MigrationActionsModule;
import com.googlecode.yadic.Container;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.propidle.migrations.modules.MigrationQueriesModule.migrationQueriesModule;

public class MigrationsModules {
    public static Sequence<UnaryFunction<Container>> migrationsModules() {
        return sequence(migrationQueriesModule(), new MigrationActionsModule());
    }
}