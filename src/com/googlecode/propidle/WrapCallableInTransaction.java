package com.googlecode.propidle;

import org.apache.lucene.index.IndexWriter;

import java.util.concurrent.Callable;

import com.googlecode.propidle.persistence.Transaction;

public class WrapCallableInTransaction implements Callable {
    private final IndexWriter indexWriter;
    private final Transaction transaction;
    private final Callable callable;

    public WrapCallableInTransaction(Transaction transaction, Callable callable) {
        this(null, transaction,  callable);
    }
    public WrapCallableInTransaction(IndexWriter indexWriter, Transaction transaction, Callable callable) {
        this.indexWriter = indexWriter;
        this.transaction = transaction;
        this.callable = callable;
    }

    public Object call() throws Exception {
        Object result;
        try {
            result = callable.call();
            if(indexWriter!=null) indexWriter.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Did not commit transaction", e);
        }
        transaction.commit();
        return result;

    }
}
