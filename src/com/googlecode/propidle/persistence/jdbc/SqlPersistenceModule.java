package com.googlecode.propidle.persistence.jdbc;

import com.googlecode.propidle.persistence.PersistenceModule;
import com.googlecode.propidle.persistence.RecordLock;
import com.googlecode.propidle.persistence.Transaction;
import com.googlecode.propidle.persistence.jdbc.hsql.HsqlRecordLock;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.totallylazy.records.sql.SqlRecords;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.yadic.Container;

import java.sql.Connection;

public class SqlPersistenceModule implements PersistenceModule, RequestScopedModule, ApplicationScopedModule {
    public SqlPersistenceModule() {
    }

    public Module addPerApplicationObjects(Container container) {
        container.add(NormalUseConnectionDetails.class);
        return this;
    }

    public Module addPerRequestObjects(Container container) {
        container.add(NormalUseConnectionDetails.class);
        container.addActivator(Connection.class, ConnectionActivator.class);
        container.add(SqlPersistence.class);
        container.addActivator(Transaction.class, container.getActivator(SqlPersistence.class));
        container.addActivator(ConnectionProvider.class, container.getActivator(SqlPersistence.class));
        container.addActivator(SqlRecords.class, SqlRecordsActivator.class);
        container.addActivator(Records.class, container.getActivator(SqlRecords.class));
        container.add(RecordLock.class, HsqlRecordLock.class);
        if(container.contains(HttpHandler.class)){
            container.decorate(HttpHandler.class, CloseConnectionDecorator.class);
        }
        return this;
    }
}
