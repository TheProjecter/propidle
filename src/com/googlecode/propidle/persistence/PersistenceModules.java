package com.googlecode.propidle.persistence;

import com.googlecode.propidle.persistence.jdbc.SqlPersistenceModule;
import com.googlecode.propidle.persistence.jdbc.hsql.HsqlModule;
import com.googlecode.propidle.persistence.jdbc.oracle.OracleModule;
import com.googlecode.propidle.persistence.memory.InMemoryPersistenceModule;
import com.googlecode.propidle.PersistenceMechanism;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.utterlyidle.modules.Module;

import static java.lang.String.format;
import java.util.Properties;

public class PersistenceModules {

    public static Sequence<Module> persistenceModules(Properties properties) {
        return persistenceModules(PersistenceMechanism.fromProperties(properties));
    }

    public static Sequence<Module> persistenceModules(PersistenceMechanism persistenceMechanism) {
        switch (persistenceMechanism) {
            case HSQL:
                return sequence(new SqlPersistenceModule(), new HsqlModule()).safeCast(Module.class);
            case ORACLE:
                return sequence(new SqlPersistenceModule(), new OracleModule()).safeCast(Module.class);
            case IN_MEMORY:
                return sequence(new InMemoryPersistenceModule()).safeCast(Module.class);
            default:
                throw new UnsupportedOperationException(format("Peristence for '%s' is not implemented", persistenceMechanism));
        }
    }
}
