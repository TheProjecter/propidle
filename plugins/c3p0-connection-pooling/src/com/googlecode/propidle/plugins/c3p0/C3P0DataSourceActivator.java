package com.googlecode.propidle.plugins.c3p0;

import com.googlecode.propidle.PersistenceMechanism;
import com.googlecode.propidle.migrations.MigrationsModule;
import com.googlecode.propidle.migrations.bootstrap.hsql.HsqlMigrationsModule;
import com.googlecode.propidle.migrations.bootstrap.oracle.OracleMigrationsModule;
import com.googlecode.propidle.persistence.jdbc.ConnectionDetails;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.migrations.MigrationQueriesModule.migrationQueriesModule;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.String.format;

public class C3P0DataSourceActivator implements Callable<DataSource>, Closeable {
    private final Properties properties;
    private final ConnectionDetails connectionDetails;
    private ComboPooledDataSource dataSource;

    public C3P0DataSourceActivator(Properties properties, ConnectionDetails connectionDetails) {
        this.properties = properties;
        this.connectionDetails = connectionDetails;
    }

    public DataSource call() throws Exception {
        if (dataSource == null) {
            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(guessDriverClass());
            dataSource.setJdbcUrl(connectionDetails.url());
            dataSource.setUser(connectionDetails.user());
            dataSource.setPassword(connectionDetails.password());
        }
        return dataSource;
    }

    private String guessDriverClass() throws PropertyVetoException {
        PersistenceMechanism persistenceMechanism = PersistenceMechanism.fromProperties(properties);
        switch (persistenceMechanism) {
            case HSQL:
                return "org.hsqldb.jdbcDriver";
            case ORACLE:
                return "oracle.jdbc.OracleDriver";
            default:
                throw new UnsupportedOperationException(format("C3P0 pooling for '%s' is not implemented", persistenceMechanism));
        }
    }

    public void close() throws IOException {
        if(dataSource != null) {
            dataSource.close();
        }
    }
}
