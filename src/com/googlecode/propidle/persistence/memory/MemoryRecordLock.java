package com.googlecode.propidle.persistence.memory;

import com.googlecode.propidle.persistence.RecordLock;
import com.googlecode.totallylazy.records.Keyword;

public class MemoryRecordLock implements RecordLock {
    public void aquire(Keyword recordName) {
        // Do nothing for now- waiting for memory transactions in TotallyLazy
    }
}
