package com.googlecode.propidle.urls;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;

public class SimpleUriGetter implements UriGetter {
    public InputStream get(URI uri, MimeType mimeType) throws IOException {
        try {
            URLConnection connection = uri.toURL().openConnection();
            connection.setRequestProperty("ACCEPT", mimeType.value());
            connection.connect();
            return connection.getInputStream();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
