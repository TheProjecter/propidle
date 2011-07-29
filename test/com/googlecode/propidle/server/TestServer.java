package com.googlecode.propidle.server;

import static com.googlecode.propidle.util.TestRecords.hsqlConfiguration;
import static com.googlecode.totallylazy.Runnables.write;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.utterlyidle.Status.OK;
import static com.googlecode.utterlyidle.io.Url.url;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_FORM_URLENCODED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.utterlyidle.ServerConfiguration;
import com.googlecode.utterlyidle.io.Url;
import com.googlecode.utterlyidle.modules.Module;

import java.io.OutputStream;
import java.util.Properties;

public class TestServer extends Server {

    public static void main(String[] args) throws Exception {
        new TestServer(true);
    }

    public TestServer() throws Exception {
        this(true);

    }

    public TestServer(boolean migrateDatabase) throws Exception {
        super(properties(), empty(Module.class));

        if (migrateDatabase) {
            migrateDatabase();
        }
    }

    private static Properties properties() {
        Properties properties = hsqlConfiguration();
        properties.setProperty(ServerConfiguration.SERVER_PORT, "8000");
        return properties;
    }

    private void migrateDatabase() {
        Url propertiesUrl = url("http://localhost:8000/migrations");
        Pair<Integer, String> callMigrationsResource = propertiesUrl.post(APPLICATION_FORM_URLENCODED, withoutRequestContent());
        assertThat(callMigrationsResource.first(), is(OK.code()));
    }

    private Callable1<OutputStream, Void> withoutRequestContent() {
        return write("".getBytes());
    }

}
