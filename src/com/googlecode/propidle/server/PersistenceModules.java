package com.googlecode.propidle.server;

import com.googlecode.propidle.PersistenceMechanism;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.migrations.bootstrap.hsql.HsqlMigrationsModule;
import com.googlecode.utterlyidle.migrations.bootstrap.oracle.OracleMigrationsModule;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.ConnectionDetails;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.SqlPersistenceModule;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.hsql.HsqlModule;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.oracle.OracleModule;
import com.googlecode.utterlyidle.migrations.persistence.memory.InMemoryPersistenceModule;
import com.googlecode.utterlyidle.modules.Module;

import java.util.Properties;

import static com.googlecode.propidle.properties.Properties.getOrFail;
import static com.googlecode.propidle.util.Modules.asRequestScopeModule;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.migrations.persistence.jdbc.ConnectionDetails.connectionDetails;
import static java.lang.String.format;

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

    public static Sequence<Module> forMigrations(Properties properties) throws Exception {
        PersistenceMechanism persistenceMechanism = PersistenceMechanism.fromProperties(properties);
        switch (persistenceMechanism) {
            case HSQL:
                return sqlModules(migrationConnectionDetails(properties), new HsqlModule()).add(asRequestScopeModule().call(new HsqlMigrationsModule()));
            case ORACLE:
                return sqlModules(migrationConnectionDetails(properties), new OracleModule()).add(asRequestScopeModule().call(new OracleMigrationsModule()));
            default:
                throw new UnsupportedOperationException(format("Migrations for '%s' is not implemented", persistenceMechanism));
        }
    }

    private static Sequence<Module> sqlModules(ConnectionDetails connectionDetails, Module... modules) {
        return sequence(new SqlPersistenceModule(connectionDetails)).
                safeCast(Module.class).
                join(sequence(modules));
    }

    public static ConnectionDetails runtimeConnection(Properties properties) {
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