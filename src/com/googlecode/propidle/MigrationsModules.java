package com.googlecode.propidle;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yadic.Container;

import java.util.Properties;

import static com.googlecode.totallylazy.Sequences.empty;
import static java.lang.String.format;

public class MigrationsModules {
    public static Sequence<Callable1<Container, Container>> migrationsModules(Properties properties) {
        return migrationsModules(PersistenceMechanism.fromProperties(properties));
    }

    public static Sequence<Callable1<Container, Container>> migrationsModules(PersistenceMechanism persistenceMechanism) {
        switch (persistenceMechanism) {
            case HSQL:
                return empty();
            case ORACLE:
                return empty();
            case IN_MEMORY:
                return empty();
            default:
                throw new UnsupportedOperationException(format("Migrations for '%s' is not implemented", persistenceMechanism));
        }
    }
}