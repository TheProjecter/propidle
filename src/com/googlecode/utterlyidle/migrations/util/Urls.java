package com.googlecode.utterlyidle.migrations.util;

import com.googlecode.totallylazy.Strings;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;

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
