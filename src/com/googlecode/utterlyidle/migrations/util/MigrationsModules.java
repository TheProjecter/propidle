package com.googlecode.utterlyidle.migrations.util;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.UnaryFunction;
import com.googlecode.utterlyidle.migrations.modules.MigrationActionsModule;
import com.googlecode.yadic.Container;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.migrations.modules.MigrationQueriesModule.migrationQueriesModule;

public class MigrationsModules {
    public static Sequence<UnaryFunction<Container>> migrationsModules() {
        return sequence(migrationQueriesModule(), new MigrationActionsModule());
    }
}