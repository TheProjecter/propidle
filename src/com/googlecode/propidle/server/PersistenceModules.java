package com.googlecode.propidle.server;

import com.googlecode.propidle.PersistenceMechanism;
import com.googlecode.propidle.persistence.jdbc.ConnectionDetails;
import static com.googlecode.propidle.persistence.jdbc.ConnectionDetails.connectionDetails;
import com.googlecode.propidle.persistence.jdbc.SqlPersistenceModule;
import com.googlecode.propidle.persistence.jdbc.hsql.HsqlModule;
import com.googlecode.propidle.persistence.jdbc.oracle.OracleModule;
import com.googlecode.propidle.persistence.memory.InMemoryPersistenceModule;
import static com.googlecode.propidle.properties.Properties.getOrFail;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.utterlyidle.modules.Module;

import static java.lang.String.format;
import java.util.Properties;

public class PersistenceModules {
    public static Sequence<Module> persistenceModules(Properties properties) {
        PersistenceMechanism persistenceMechanism = PersistenceMechanism.fromProperties(properties);
        switch (persistenceMechanism) {
            case HSQL:
                return sqlModules(runtimeConnection(properties), new HsqlModule());
            case ORACLE:
                return sqlModules(runtimeConnection(properties), new OracleModule());
            case IN_MEMORY:
                return sequence(new InMemoryPersistenceModule()).safeCast(Module.class);
            default:
                throw new UnsupportedOperationException(format("Peristence for '%s' is not implemented", persistenceMechanism));
        }
    }

    public static Sequence<Module> forMigrations(Properties properties) {
        PersistenceMechanism persistenceMechanism = PersistenceMechanism.fromProperties(properties);
        switch (persistenceMechanism) {
            case HSQL:
                return sqlModules(migrationConnectionDetails(properties), new HsqlModule());
            case ORACLE:
                return sqlModules(migrationConnectionDetails(properties), new OracleModule());
            default:
                throw new UnsupportedOperationException(format("Migrations for '%s' is not implemented", persistenceMechanism));
        }
    }

    private static Sequence<Module> sqlModules(ConnectionDetails connectionDetails, Module... modules) {
        return sequence(new SqlPersistenceModule(connectionDetails)).
                safeCast(Module.class).
                join(sequence(modules));
    }

    private static ConnectionDetails runtimeConnection(Properties properties) {
        return connectionDetails(getOrFail(properties, Server.JDBC_URL),
                                 getOrFail(properties, Server.JDBC_USER),
                                 getOrFail(properties, Server.JDBC_PASSWORD));
    }

    private static ConnectionDetails migrationConnectionDetails(Properties properties) {
        return connectionDetails(getOrFail(properties, Server.JDBC_URL),
                                 getOrFail(properties, Server.MIGRATION_JDBC_USER),
                                 getOrFail(properties, Server.MIGRATION_JDBC_PASSWORD));
    }
}
