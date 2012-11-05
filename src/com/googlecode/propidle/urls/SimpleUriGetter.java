package com.googlecode.propidle.urls;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;

public class SimpleUriGetter implements UriGetter {

    private final int timeout;

    public SimpleUriGetter(int timeoutInMillis) {
        this.timeout = timeoutInMillis;
    }

    public SimpleUriGetter() {
        this.timeout = 0;
    }

    public InputStream get(URI uri, MimeType mimeType) throws IOException {
        try {
            URLConnection connection = uri.toURL().openConnection();
            connection.setConnectTimeout(timeout);
            connection.setRequestProperty("ACCEPT", mimeType.value());
            connection.connect();
            return connection.getInputStream();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
