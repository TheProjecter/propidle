package com.googlecode.propidle;

import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.propidle.PersistenceMechanism;
import com.googlecode.propidle.migrations.bootstrap.hsql.HsqlMigrationsModule;
import com.googlecode.propidle.migrations.bootstrap.oracle.OracleMigrationsModule;
import com.googlecode.propidle.migrations.MigrationsModule;

import java.util.Properties;
import static java.lang.String.format;

public class MigrationsModules {
    public static Sequence<Module> migrationsModules(Properties properties) {
        return migrationsModules(PersistenceMechanism.fromProperties(properties));
    }

    public static Sequence<Module> migrationsModules(PersistenceMechanism persistenceMechanism) {
        switch(persistenceMechanism){
            case HSQL:
                return Sequences.sequence(new MigrationsModule(), new HsqlMigrationsModule()).safeCast(Module.class);
            case ORACLE:
                return sequence(new MigrationsModule(), new OracleMigrationsModule()).safeCast(Module.class);
            case IN_MEMORY:
                return empty(Module.class);
            default :
                throw new UnsupportedOperationException(format("Migrations for '%s' is not implemented", persistenceMechanism));
        }
    }
}
