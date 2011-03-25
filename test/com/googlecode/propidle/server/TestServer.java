package com.googlecode.propidle.server;

import static com.googlecode.propidle.util.TestRecords.hsqlConfiguration;
import static com.googlecode.totallylazy.Runnables.write;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.utterlyidle.Status.OK;
import static com.googlecode.utterlyidle.io.Url.url;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.googlecode.propidle.util.TestRecords;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.utterlyidle.io.Url;
import com.googlecode.utterlyidle.modules.Module;

import java.io.OutputStream;
import java.util.Properties;

public class TestServer extends Server {
    public static void main(String[] args) throws Exception {
        new TestServer(8000, false);
    }

    public TestServer(int port) throws Exception {
        this(port, true);

    }

    public TestServer(int port, boolean migrateDatabase) throws Exception {
        super(propertiesFor(port), empty(Module.class));

        if (migrateDatabase) {
            migrateDatabase();
        }
    }

    private void migrateDatabase() {
        Url propertiesUrl = url("http://localhost:8000/migrations");
        Pair<Integer, String> callMigrationsResource = propertiesUrl.post(APPLICATION_FORM_URLENCODED, withoutRequestContent());
        assertThat(callMigrationsResource.first(), is(OK.code()));
    }

    private Callable1<OutputStream, Void> withoutRequestContent() {
        return write("".getBytes());
    }

    private static Properties propertiesFor(int port) {
        Properties properties = hsqlConfiguration();;
        properties.setProperty(PORT, String.valueOf(port));
        return properties;
    }
}
