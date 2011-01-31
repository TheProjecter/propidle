package com.googlecode.propidle.persistence.jdbc;

import com.googlecode.propidle.persistence.PersistenceModule;
import com.googlecode.propidle.persistence.RecordLock;
import com.googlecode.propidle.persistence.Transaction;
import com.googlecode.propidle.persistence.jdbc.hsql.HsqlRecordLock;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

import java.sql.Connection;

public class SqlPersistenceModule implements PersistenceModule, RequestScopedModule {
    public final ConnectionDetails connectionDetails;

    public SqlPersistenceModule(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public Module addPerRequestObjects(Container container) {
        container.addInstance(ConnectionDetails.class, connectionDetails);
        container.addActivator(Connection.class, ConnectionActivator.class);
        container.add(SqlPersistence.class);
        container.addActivator(Transaction.class, container.getActivator(SqlPersistence.class));
        container.addActivator(ConnectionProvider.class, container.getActivator(SqlPersistence.class));
        container.addActivator(Records.class, SqlRecordsActivator.class);
        container.add(RecordLock.class, HsqlRecordLock.class);
        container.decorate(HttpHandler.class, CloseConnectionDecorator.class);
        return this;
    }
}
