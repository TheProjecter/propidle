package com.googlecode.propidle.client.cache;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.propidle.server.Server;
import com.googlecode.propidle.server.TestServer;
import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.handlers.RedirectHttpHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static com.googlecode.propidle.client.cache.CachingDynamicProperties.caching;
import static com.googlecode.propidle.client.loaders.PropertiesAtUrl.propertiesAtUrl;
import static com.googlecode.propidle.util.Sha1.sha1;
import static com.googlecode.totallylazy.Uri.uri;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.Status.OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CacheTest {
    private Server server;
    private final String serverUrl = "http://localhost:8000/";

    @Before
    public void startServer() throws Exception {
        server = new TestServer(true, true);
    }

    @After
    public void stopServer() throws Exception {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    @Test
    public void loadsPropertiesFromCacheIfServerGoesDown() throws Exception {
        HttpHandler handler = new RedirectHttpHandler(new ClientHttpHandler());
        Uri propertiesUrl = uri(serverUrl + "properties/test");

        Response response = handler.handle(post(propertiesUrl).form("properties", "test:hello").build());
        assertThat(response.status(), is(OK));

        URL url = propertiesUrl.toURL();
        DynamicProperties dynamicProperties = new DynamicProperties(caching(cacheFile(url), propertiesAtUrl(url)));
        dynamicProperties.reload();

        assertThat(dynamicProperties.snapshot().get("test").toString(), is(equalTo("hello")));


        stopServer();

        dynamicProperties.reload();
        assertThat(dynamicProperties.snapshot().get("test").toString(), is(equalTo("hello")));
    }

    private File cacheFile(URL url) {
        return new File("/tmp/" + sha1(url.toString()));
    }

}