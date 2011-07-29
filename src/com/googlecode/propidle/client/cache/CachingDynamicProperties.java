package com.googlecode.propidle.client.cache;

import com.googlecode.totallylazy.Callable1;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Closeables.using;

public class CachingDynamicProperties {
    public static Callable<Properties> caching(final File cache, final Callable<Properties> callable) {
        return new Callable<Properties>() {
            public Properties call() throws Exception {
                try {
                    return toCache(cache,callable.call());
                } catch (Exception e) {
                    return fromCache(cache);
                }
            }

            private synchronized Properties toCache(File cache, final Properties properties) throws IOException {
                return using(new FileWriter(cache), new Callable1<FileWriter, Properties>() {
                    public Properties call(FileWriter fileWriter) throws Exception {
                        properties.store(fileWriter, null);
                        return properties;
                    }
                });
            }
        };
    }

    private static Properties fromCache(File cache) throws FileNotFoundException {
        return using(new FileReader(cache), new Callable1<FileReader, Properties>() {
            public Properties call(FileReader fileReader) throws Exception {
                Properties properties = new Properties();
                properties.load(fileReader);
                return properties;
            }
        });
    }

}
