package com.googlecode.propidle.server;

import static com.googlecode.propidle.server.Server.PORT;
import static com.googlecode.propidle.util.TestRecords.inMemoryDatabaseConfiguraton;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Runnable1;
import com.googlecode.totallylazy.Strings;
import static com.googlecode.utterlyidle.Status.OK;
import static com.googlecode.utterlyidle.Status.SEE_OTHER;
import com.googlecode.utterlyidle.io.Url;
import static com.googlecode.utterlyidle.io.Url.url;
import static com.googlecode.utterlyidle.io.Url.writeBytes;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import java.io.InputStream;
import java.util.Properties;

public class ServerTest {
    private Server server;

    @Before
    public void startServer() throws Exception {
        server = new TestServer(8000);
    }

    @After
    public void stopServer() throws Exception {
        if (server != null) server.stop();
    }

    @Test
    public void shouldPassSmokeTest() throws Exception {
        Url propertiesUrl = url("http://localhost:8000/properties/test");

        Pair<Integer, String> post = propertiesUrl.post(APPLICATION_FORM_URLENCODED, writeBytes("properties=test:hello".getBytes()));
        assertThat(post.first(), is(OK.code()));

        ResponseAsString getResponse = new ResponseAsString();
        Pair<Integer, String> get = propertiesUrl.get(TEXT_PLAIN, getResponse);

        assertThat(get.first(), is(OK.code()));
        assertThat(getResponse.value(), is("# /properties/test?revision=0\ntest=hello\n"));
    }

    public static class ResponseAsString implements Runnable1<InputStream> {
        private String value;

        public void run(InputStream inputStream) {
            value = Strings.toString(inputStream);
        }

        public String value() {
            return value;
        }
    }
}
