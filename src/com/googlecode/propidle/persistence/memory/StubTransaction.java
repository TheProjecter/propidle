package com.googlecode.propidle.persistence.memory;

import com.googlecode.propidle.persistence.Transaction;

public class StubTransaction implements Transaction{
    public void commit() {
    }

    public void rollback() {
    }
}
