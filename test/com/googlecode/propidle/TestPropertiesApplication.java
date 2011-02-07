package com.googlecode.propidle;

import acceptance.steps.CloseTransaction;
import static com.googlecode.propidle.aliases.AliasesFromRecords.defineAliasRecord;
import static com.googlecode.propidle.authorisation.users.UsersFromRecords.defineUsersRecord;
import static com.googlecode.propidle.authorisation.groups.GroupsFromRecords.defineGroupsRecord;
import static com.googlecode.propidle.authorisation.groups.GroupMembershipsFromRecords.defineGroupMembershipsRecord;
import static com.googlecode.propidle.authorisation.permissions.GroupPermissionsFromRecords.defineGroupPermissionsRecord;
import com.googlecode.propidle.persistence.memory.InMemoryPersistenceModule;
import com.googlecode.propidle.server.PropertiesApplication;
import static com.googlecode.propidle.authentication.SessionsFromRecords.defineSessionsRecord;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.defineChangesRecord;
import static com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords.defineHighestRevisionRecord;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import com.googlecode.utterlyidle.modules.Module;
import org.apache.lucene.store.RAMDirectory;

import java.util.concurrent.Callable;

public class TestPropertiesApplication extends PropertiesApplication {
    public TestPropertiesApplication(Module... extraModules) throws Exception {
        super(
                //                TemporaryIndex.directory(new File("/Users/mattsavage/Desktop/lucene")),
                new RAMDirectory(),
                new InMemoryPersistenceModule());
//                new SqlPersistenceModule(connectionDetails("jdbc:hsqldb:mem:" + UUID.randomUUID(), "SA", "")));
        for (Module extraModule : extraModules) {
            add(extraModule);
        }
        defineRecords();
    }

    private void defineRecords() throws Exception {
        inTransaction(DefineRecords.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T inTransaction(Class<? extends Callable<T>> step) throws Exception {
        Container request = new SimpleContainer(createRequestScope());
        request.add(Callable.class, step);
        request.add(CloseTransaction.class);
        return (T) request.get(CloseTransaction.class).call();
    }

    public static class DefineRecords implements Callable<Void> {
        private final Records records;

        public DefineRecords(Records records) {
            this.records = records;
        }

        public Void call() throws Exception {
            defineUsersRecord(records);
            defineSessionsRecord(records);
            defineHighestRevisionRecord(records);
            defineChangesRecord(records);
            defineAliasRecord(records);
            defineGroupsRecord(records);
            defineGroupMembershipsRecord(records);
            defineGroupPermissionsRecord(records);
            return null;
        }
    }
}
