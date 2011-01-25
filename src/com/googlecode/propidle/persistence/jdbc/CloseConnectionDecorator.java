package com.googlecode.propidle.persistence.jdbc;

import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;

import java.sql.Connection;

public class CloseConnectionDecorator implements HttpHandler {
    private final HttpHandler decorated;
    private final Connection connection;

    public CloseConnectionDecorator(HttpHandler decorated, Connection connection) {
        this.decorated = decorated;
        this.connection = connection;
    }

    public void handle(Request request, Response response) throws Exception {
        try {
            decorated.handle(request, response);
        } finally {
            connection.close();
        }
    }
}
