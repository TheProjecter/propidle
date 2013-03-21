package com.googlecode.propidle.migrations.persistence.memory;

import com.googlecode.lazyrecords.Definition;
import com.googlecode.propidle.migrations.persistence.RecordLock;
import com.googlecode.propidle.migrations.persistence.RecordLock;

public class MemoryRecordLock implements RecordLock {
    public void aquire(Definition recordName) {
        // Do nothing for now- waiting for memory transactions in TotallyLazy
    }
}
