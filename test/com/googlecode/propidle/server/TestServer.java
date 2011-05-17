package com.googlecode.propidle.server;

import static com.googlecode.propidle.util.TestRecords.hsqlConfiguration;
import static com.googlecode.totallylazy.Runnables.write;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.utterlyidle.ServerConfiguration.SERVER_URL;
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
    public static final String SERVER_URL = "http://localhost:8000/";

    public static void main(String[] args) throws Exception {
        new TestServer(SERVER_URL, true);
    }

    public TestServer(String serverUrl) throws Exception {
        this(serverUrl, true);

    }

    public TestServer(String serverUrl, boolean migrateDatabase) throws Exception {
        super(propertiesFor(serverUrl), empty(Module.class));

        if (migrateDatabase) {
            migrateDatabase();
        }
    }

    private void migrateDatabase() {
        Url propertiesUrl = url(SERVER_URL + "migrations");
        Pair<Integer, String> callMigrationsResource = propertiesUrl.post(APPLICATION_FORM_URLENCODED, withoutRequestContent());
        assertThat(callMigrationsResource.first(), is(OK.code()));
    }

    private Callable1<OutputStream, Void> withoutRequestContent() {
        return write("".getBytes());
    }

    private static Properties propertiesFor(String serverUrl) {
        Properties properties = hsqlConfiguration();
        properties.setProperty(ServerConfiguration.SERVER_URL, serverUrl);
        return properties;
    }
}
