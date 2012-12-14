package com.googlecode.propidle.server;

import com.googlecode.lazyrecords.IgnoreLogger;
import com.googlecode.lazyrecords.Logger;
import com.googlecode.lazyrecords.Records;
import com.googlecode.lazyrecords.Transaction;
import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.lazyrecords.sql.SqlTransaction;
import com.googlecode.lazyrecords.sql.mappings.SqlMappings;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.ConnectionDetails;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

import static com.googlecode.propidle.properties.Properties.getOrFail;
import static com.googlecode.utterlyidle.migrations.persistence.jdbc.ConnectionDetails.connectionDetails;

public class SqlModule implements RequestScopedModule, ApplicationScopedModule {

    private final Properties properties;

    public SqlModule(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Container addPerApplicationObjects(Container container) throws Exception {
        container.addInstance(ConnectionDetails.class, connectionDetails(getOrFail(properties, Server.JDBC_URL),
                        getOrFail(properties, Server.JDBC_USER),
                        getOrFail(properties, Server.JDBC_PASSWORD)));
        return container.addActivator(DataSource.class, DataSourceActivator.class);
    }

    @Override
    public Container addPerRequestObjects(Container container) throws Exception {
        container.addActivator(Connection.class, ConnectionActivator.class);
        container.add(Transaction.class, SqlTransaction.class);
        container.add(SqlRecords.class);
        container.add(Logger.class, IgnoreLogger.class);
        container.add(SqlMappings.class);
        container.addActivator(Records.class, container.getActivator(SqlRecords.class));
        return container;
    }

}
