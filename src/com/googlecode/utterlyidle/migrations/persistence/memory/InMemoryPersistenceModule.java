package com.googlecode.utterlyidle.migrations.persistence.memory;

import com.googlecode.lazyrecords.Records;
import com.googlecode.lazyrecords.Transaction;
import com.googlecode.lazyrecords.memory.MemoryRecords;
import com.googlecode.utterlyidle.migrations.persistence.PersistenceModule;
import com.googlecode.utterlyidle.migrations.persistence.RecordLock;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.yadic.Container;

public class InMemoryPersistenceModule implements PersistenceModule, ApplicationScopedModule {
    public Container addPerApplicationObjects(Container container) {
        return container.
                add(Records.class, MemoryRecords.class).
                add(Transaction.class, StubTransaction.class).
                add(RecordLock.class, MemoryRecordLock.class);
    }
}
