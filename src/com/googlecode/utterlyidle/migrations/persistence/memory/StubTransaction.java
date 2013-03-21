package com.googlecode.utterlyidle.migrations.persistence.memory;

import com.googlecode.lazyrecords.Transaction;


public class StubTransaction implements Transaction {
    public void commit() {
    }

    public void rollback() {
    }
}
