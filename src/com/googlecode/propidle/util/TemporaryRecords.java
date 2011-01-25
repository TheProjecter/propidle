package com.googlecode.propidle.util;

import com.googlecode.totallylazy.records.memory.MemoryRecords;

public class TemporaryRecords extends MemoryRecords {
    public static TemporaryRecords temporaryRecords() {
        return new TemporaryRecords();
    }

    private TemporaryRecords() {
        super();
    }

}
