package com.googlecode.propidle.persistence.memory;

import com.googlecode.propidle.persistence.PersistenceModule;
import com.googlecode.propidle.persistence.RecordLock;
import com.googlecode.propidle.persistence.Transaction;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.totallylazy.records.memory.MemoryRecords;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;

public class InMemoryPersistenceModule implements PersistenceModule, ApplicationScopedModule {
    public Module addPerApplicationObjects(Container container) {
        container.add(Records.class, MemoryRecords.class);
        container.add(Transaction.class, StubTransaction.class);
        container.add(RecordLock.class, MemoryRecordLock.class);
        return this;
    }
}
