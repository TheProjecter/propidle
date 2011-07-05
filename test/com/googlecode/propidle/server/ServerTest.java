package com.googlecode.propidle.server;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.Strings;

import static com.googlecode.totallylazy.Runnables.write;
import static com.googlecode.utterlyidle.Status.OK;
import com.googlecode.utterlyidle.io.Url;
import static com.googlecode.utterlyidle.io.Url.url;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.googlecode.utterlyidle.MediaType.APPLICATION_FORM_URLENCODED;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;
import java.io.InputStream;

public class ServerTest {
    private Server server;
    private final String serverUrl = "http://localhost:8000/";

    @Before
    public void startServer() throws Exception {
        server = new TestServer();
    }

    @After
    public void stopServer() throws Exception {
        if (server != null) server.stop();
    }

    @Test
    public void shouldPassSmokeTest() throws Exception {
        Url propertiesUrl = url(serverUrl + "properties/test");

        Pair<Integer, String> post = propertiesUrl.post(APPLICATION_FORM_URLENCODED, write("properties=test:hello".getBytes()));
        assertThat(post.first(), is(OK.code()));

        ResponseAsString getResponse = new ResponseAsString();
        Pair<Integer, String> get = propertiesUrl.get(TEXT_PLAIN, getResponse);

        assertThat(get.first(), is(OK.code()));
        assertThat(getResponse.value(), is("# /properties/test?revision=0\ntest=hello\n"));
    }

    public static class ResponseAsString implements Callable1<InputStream, Void> {
        private String value;

        public Void call(InputStream inputStream) {
            value = Strings.toString(inputStream);
            return Runnables.VOID;
        }

        public String value() {
            return value;
        }
    }
}
