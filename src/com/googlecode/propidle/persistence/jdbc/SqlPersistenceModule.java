package com.googlecode.propidle.persistence.jdbc;

import com.googlecode.propidle.persistence.PersistenceModule;
import com.googlecode.propidle.persistence.Transaction;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.totallylazy.records.sql.SqlRecords;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.yadic.Container;

import java.sql.Connection;

public class SqlPersistenceModule implements PersistenceModule, RequestScopedModule, ApplicationScopedModule {
    private final ConnectionDetails connectionDetails;

    public SqlPersistenceModule(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public Module addPerApplicationObjects(Container container) {
        container.addInstance(ConnectionDetails.class, connectionDetails);
        return this;
    }

    public Module addPerRequestObjects(Container container) {
        container.addActivator(Connection.class, ConnectionActivator.class);
        container.add(Transaction.class, SqlPersistence.class);
        container.add(SqlRecords.class);
        container.addActivator(Records.class, container.getActivator(SqlRecords.class));
        return this;
    }
}
