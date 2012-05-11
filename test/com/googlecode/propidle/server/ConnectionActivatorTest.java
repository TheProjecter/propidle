package com.googlecode.propidle.server;

import com.googlecode.lazyrecords.sql.ReadOnlyConnection;
import com.googlecode.utterlyidle.RequestBuilder;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.Test;

import java.sql.Connection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ConnectionActivatorTest {

    @Test
    public void shouldReturnReadonlyConnectionForGetRequest() throws Exception {
        Connection connection = connectionFor(RequestBuilder.get("whatever"));
        assertThat(connection, is(instanceOf(ReadOnlyConnection.class)));
    }

    @Test
    public void shouldNotReturnAReadOnlyConnectionForPostRequest() throws Exception {
        Connection connection = connectionFor(RequestBuilder.post("whatever"));
        assertThat(connection, is(not(instanceOf(ReadOnlyConnection.class))));
    }

    private Connection connectionFor(RequestBuilder requestBuilder) throws Exception {
        return new ConnectionActivator(new JDBCDataSource(), requestBuilder.build()).call();
    }
}
