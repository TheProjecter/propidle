package com.googlecode.propidle.server;

import com.googlecode.propidle.scheduling.IgnoreScheduler;
import com.googlecode.propidle.scheduling.ScheduleTask;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.ServerConfiguration;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;

import java.io.OutputStream;
import java.util.Properties;

import static com.googlecode.propidle.util.TestRecords.hsqlConfiguration;
import static com.googlecode.totallylazy.Runnables.write;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Uri.uri;
import static com.googlecode.utterlyidle.Status.OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestServer extends Server {

    public static void main(String[] args) throws Exception {
        new TestServer(true, false);
    }

    public TestServer() throws Exception {
        this(true, false);

    }

    public TestServer(boolean migrateDatabase, boolean removeTasks) throws Exception {
        super(properties(), removeTasks ? Sequences.<Module>sequence(new DisableTasks()) : empty(Module.class));

        if (migrateDatabase) {
            migrateDatabase();
        }
    }

    private static Properties properties() {
        Properties properties = hsqlConfiguration();
        properties.setProperty(ServerConfiguration.SERVER_PORT, "8000");
        return properties;
    }

    private void migrateDatabase() throws Exception {
        ClientHttpHandler clientHttpHandler = new ClientHttpHandler();
        Uri propertiesUrl = uri("http://localhost:8000/migrations");

        Response response = clientHttpHandler.handle(RequestBuilder.post(propertiesUrl).build());
        assertThat(response.status(), is(OK));
    }

    private Callable1<OutputStream, Void> withoutRequestContent() {
        return write("".getBytes());
    }

    public static class DisableTasks implements ApplicationScopedModule {
        public Module addPerApplicationObjects(Container container) throws Exception {
            container.replace(ScheduleTask.class, IgnoreScheduler.class);
            return this;
        }
    }
}
