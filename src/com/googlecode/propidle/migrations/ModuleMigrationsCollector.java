package com.googlecode.propidle.migrations;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yadic.Resolver;
import com.googlecode.yadic.resolvers.Resolvers;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class ModuleMigrationsCollector {
    private final List<Class<? extends ModuleMigrations>> moduleMigrations = new ArrayList<Class<? extends ModuleMigrations>>();

    public void add(Class<? extends ModuleMigrations> moduleMigrationsClass) {
        moduleMigrations.add(moduleMigrationsClass);
    }

    public Sequence<ModuleMigrations> moduleMigrations(Resolver resolver) {
        return sequence(moduleMigrations).map(resolve(resolver)).safeCast(ModuleMigrations.class);
    }

    private Callable1<Class, Object> resolve(final Resolver resolver) {
        return new Callable1<Class, Object>(){

            public Object call(Class aClass) throws Exception {
                return Resolvers.create(aClass, resolver).resolve(aClass);
            }
        };
    }

}
