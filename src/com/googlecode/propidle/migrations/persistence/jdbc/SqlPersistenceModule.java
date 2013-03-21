package com.googlecode.propidle.migrations.persistence.jdbc;

import com.googlecode.lazyrecords.IgnoreLogger;
import com.googlecode.lazyrecords.Logger;
import com.googlecode.lazyrecords.Records;
import com.googlecode.lazyrecords.Transaction;
import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.lazyrecords.sql.SqlSchema;
import com.googlecode.lazyrecords.sql.SqlTransaction;
import com.googlecode.lazyrecords.sql.grammars.AnsiSqlGrammar;
import com.googlecode.lazyrecords.sql.grammars.SqlGrammar;
import com.googlecode.propidle.migrations.persistence.PersistenceModule;
import com.googlecode.propidle.migrations.persistence.PersistenceModule;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

import java.sql.Connection;

public class SqlPersistenceModule implements PersistenceModule, RequestScopedModule, ApplicationScopedModule {
    private final ConnectionDetails connectionDetails;

    public SqlPersistenceModule(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public Container addPerApplicationObjects(Container container) {
        return container.addInstance(ConnectionDetails.class, connectionDetails);
    }

    public Container addPerRequestObjects(Container container) {
        return container.
                addActivator(Connection.class, ConnectionActivator.class).
                add(SqlSchema.class).
                add(SqlGrammar.class, AnsiSqlGrammar.class).
                add(Logger.class, IgnoreLogger.class).
                add(SqlRecords.class).
                add(Transaction.class, SqlTransaction.class).
                addActivator(Records.class, container.getActivator(SqlRecords.class));
    }
}
