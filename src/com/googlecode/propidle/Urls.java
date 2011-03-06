package com.googlecode.propidle;

import com.googlecode.totallylazy.Strings;

import java.util.concurrent.Callable;
import java.net.URL;
import java.io.InputStream;

public class Urls {
    public static Callable<String> getUrl(final URL url) {
        return new Callable<String>() {
            public String call() throws Exception {
                InputStream stream = url.openStream();
                try {
                    return Strings.toString(stream);
                } finally {
                    stream.close();
                }
            }
        };
    }
}
