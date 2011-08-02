package com.googlecode.propidle.client.loaders;

import com.googlecode.propidle.urls.MimeType;
import com.googlecode.propidle.urls.UriGetter;
import com.googlecode.propidle.urls.SimpleUriGetter;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.totallylazy.Sequences.sequence;

import com.googlecode.propidle.client.PropertyLoadingException;

public class PropertiesAtUrl implements Callable<Properties> {
    private final com.googlecode.totallylazy.Sequence<URL> urls;
    private final UriGetter uriGetter;

    public static PropertiesAtUrl propertiesAtUrl(UriGetter uriGetter, URL... url) {
        return new PropertiesAtUrl(uriGetter, url);
    }

    public static PropertiesAtUrl propertiesAtUrl(URL... urls) {
        return new PropertiesAtUrl(new SimpleUriGetter(), urls);
    }

    protected PropertiesAtUrl(UriGetter uriGetter, URL... urls) {

        this.urls = sequence(urls);
        this.uriGetter = uriGetter;
    }

    public Properties call() throws RuntimeException {

        for (URL url : urls) {
            try {
                InputStream stream = uriGetter.get(url.toURI(), MimeType.TEXT_PLAIN);
                return properties(stream);
            } catch (Throwable e) {
                System.err.println(String.format("Could not load properties at %s", url));
                e.printStackTrace();
            }
        }
        throw new PropertyLoadingException(String.format("Could not load properties %s ", urls.toString()));
    }
}
