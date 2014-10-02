package com.googlecode.propidle.server;

import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.handlers.RedirectHttpHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.googlecode.totallylazy.Uri.uri;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;
import static com.googlecode.utterlyidle.Status.OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ServerTest {
    private Server server;
    private final String serverUrl = "http://localhost:8000/";

    @Before
    public void startServer() throws Exception {
        server = new TestServer(true, true);
    }

    @After
    public void stopServer() throws Exception {
        if (server != null) server.stop();
    }

    @Test
    public void shouldPassSmokeTest() throws Exception {
        HttpHandler clientHttpHandler = new RedirectHttpHandler(new ClientHttpHandler());
        Uri propertiesUrl = uri(serverUrl + "properties/test");

        Response response = clientHttpHandler.handle(RequestBuilder.post(propertiesUrl).form("properties", "test:hello").build());

        assertThat(response.status(), is(OK));

        response = clientHttpHandler.handle(RequestBuilder.get(propertiesUrl).accepting(TEXT_PLAIN).build());

        assertThat(response.status(), is(OK));
        assertThat(response.entity().toString(), is("test=hello\r"));
    }

}
