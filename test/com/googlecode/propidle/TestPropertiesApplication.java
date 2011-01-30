package com.googlecode.propidle;

import com.googlecode.propidle.persistence.Transaction;
import com.googlecode.propidle.persistence.jdbc.SqlPersistenceModule;
import com.googlecode.propidle.server.PropertiesApplication;
import com.googlecode.propidle.util.TemporaryIndex;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.yadic.Container;
import org.apache.lucene.index.IndexWriter;

import static com.googlecode.propidle.aliases.AliasesFromRecords.defineAliasRecord;
import static com.googlecode.propidle.authorisation.users.UsersFromRecords.defineUsersRecord;
import static com.googlecode.propidle.persistence.jdbc.ConnectionDetails.connectionDetails;
import static com.googlecode.propidle.server.sessions.SessionsFromRecords.defineSessionsRecord;
import static com.googlecode.propidle.versioncontrol.changes.ChangesFromRecords.defineChangesRecord;
import static com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords.defineHighestRevisionRecord;

public class TestPropertiesApplication extends PropertiesApplication {
    private static boolean recordsDefined;

    public TestPropertiesApplication() {
        super(
                //                TemporaryIndex.directory(new File("/Users/mattsavage/Desktop/lucene")),
                TemporaryIndex.emptyFileSystemDirectory(),
                new SqlPersistenceModule(connectionDetails("jdbc:hsqldb:mem:totallylazy", "SA", "")));
    }

    public void defineRecords() {
        inTransaction(
                new Callable1<Container, Void>() {
                    public Void call(Container container) throws Exception {
                        if (recordsDefined) return null;

                        Records records = container.get(Records.class);
                        defineUsersRecord(records);
                        defineSessionsRecord(records);
                        defineHighestRevisionRecord(records);
                        defineChangesRecord(records);
                        defineAliasRecord(records);

                        recordsDefined = true;
                        return null;
                    }
                });
    }

    public <T> T inTransaction(Callable1<Container, T> action) {
        Container requestScope = createRequestScope();
        return inTransaction(requestScope, action);
    }

    public static <T> T inTransaction(Container container, Callable1<Container, T> action) {
        Transaction transaction = startTransaction(container);

        T result;
        try {
            result = action.call(container);
            container.get(IndexWriter.class).commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Could not commit transaction", e);
        }
        transaction.commit();
        return result;
    }

    private static Transaction startTransaction(Container request) {
        return request.get(Transaction.class);
    }
}
