package com.googlecode.propidle.migrations.util;

import com.googlecode.lazyrecords.Transaction;

import java.util.concurrent.Callable;

public class WrapCallableInTransaction implements Callable {
    private final Transaction transaction;
    private final Callable callable;

    public WrapCallableInTransaction(Transaction transaction, Callable callable) {
        this.transaction = transaction;
        this.callable = callable;
    }

    public Object call() throws Exception {
        Object result;
        try {
            result = callable.call();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Did not commit transaction", e);
        }
        transaction.commit();
        return result;

    }
}
