package com.googlecode.propidle.server;

import com.googlecode.lazyrecords.sql.ReadOnlyConnection;
import com.googlecode.totallylazy.Closeables;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.Request;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.Requests.method;
import static com.googlecode.utterlyidle.annotations.HttpMethod.GET;
import static com.googlecode.utterlyidle.annotations.HttpMethod.HEAD;

public class ConnectionActivator implements Callable<Connection>, Closeable {

    private final DataSource dataSource;
    private final Option<Request> request;
    private Connection connection;

    public ConnectionActivator(DataSource dataSource) {
        this(dataSource, Option.<Request>none());
    }

    public ConnectionActivator(DataSource dataSource, Request request) {
        this(dataSource, Option.option(request));
    }

    private ConnectionActivator(DataSource dataSource, Option<Request> request) {
        this.dataSource = dataSource;
        this.request = request;
    }

    @Override
    public Connection call() throws Exception {
        return connection = connection();
    }

    @Override
    public void close() throws IOException {
        Closeables.close(connection);
    }

    private Connection connection() throws SQLException {
        if (sequence(some(GET), some(HEAD)).contains(request.map(method()))) {
            return new ReadOnlyConnection(dataSource);
        }
        return dataSource.getConnection();
    }
}
