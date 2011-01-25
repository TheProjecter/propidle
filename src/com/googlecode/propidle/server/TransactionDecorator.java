package com.googlecode.propidle.server;

import com.googlecode.propidle.persistence.Transaction;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;

public class TransactionDecorator implements HttpHandler {
    private final HttpHandler decorated;
    private final Transaction transaction;

    public TransactionDecorator(HttpHandler decorated, Transaction transaction) {
        this.decorated = decorated;
        this.transaction = transaction;
    }

    public void handle(Request request, Response response) throws Exception {
        try {
            decorated.handle(request, response);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
