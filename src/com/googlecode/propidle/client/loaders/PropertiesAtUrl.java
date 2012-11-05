package com.googlecode.propidle.client.loaders;

import com.googlecode.propidle.client.PropertyLoadingException;
import com.googlecode.propidle.urls.SimpleUriGetter;
import com.googlecode.propidle.urls.UriGetter;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.urls.MimeType.TEXT_PLAIN;

public class PropertiesAtUrl implements Callable<Properties> {
    private final URL url;
    private final UriGetter uriGetter;

    public static PropertiesAtUrl propertiesAtUrl(URL url, int timeoutInMillis) {
        return new PropertiesAtUrl(url, timeoutInMillis);
    }

    public static PropertiesAtUrl propertiesAtUrl(URL url) {
        return new PropertiesAtUrl(url, 0);
    }

    public static PropertiesAtUrl propertiesAtUrl(URL url, UriGetter uriGetter) {
        return new PropertiesAtUrl(url, uriGetter);
    }

    protected PropertiesAtUrl(URL url, int timeoutInMillis) {
        this(url, new SimpleUriGetter(timeoutInMillis));
    }

    protected PropertiesAtUrl(URL url, UriGetter uriGetter) {
        this.url = url;
        this.uriGetter = uriGetter;
    }

    public Properties call() throws Exception {
        try {
            InputStream stream = uriGetter.get(url.toURI(), TEXT_PLAIN);
            return properties(stream);
        } catch (Throwable e) {
            throw new PropertyLoadingException(String.format("Could not load properties %s", url), e);
        }
    }
}
