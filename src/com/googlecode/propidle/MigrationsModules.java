package com.googlecode.propidle;

import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sequences.empty;

import com.googlecode.propidle.migrations.MigrationsModule;
import com.googlecode.propidle.migrations.bootstrap.hsql.HsqlMigrationsModule;
import com.googlecode.propidle.migrations.bootstrap.oracle.OracleMigrationsModule;
import com.googlecode.propidle.util.Callables;
import static com.googlecode.propidle.util.Callables.chain;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yadic.Container;

import static java.lang.String.format;
import java.util.Properties;

public class MigrationsModules {
    public static Sequence<Callable1<Container, Container>> migrationsModules(Properties properties) {
        return migrationsModules(PersistenceMechanism.fromProperties(properties));
    }

    public static Sequence<Callable1<Container, Container>> migrationsModules(PersistenceMechanism persistenceMechanism) {
        switch (persistenceMechanism) {
            case HSQL:
                return sequence(new MigrationsModule(), new HsqlMigrationsModule());
            case ORACLE:
                return sequence(new MigrationsModule(), new OracleMigrationsModule());
            case IN_MEMORY:
                return empty();
            default:
                throw new UnsupportedOperationException(format("Migrations for '%s' is not implemented", persistenceMechanism));
        }
    }

}
