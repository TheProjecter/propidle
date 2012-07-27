package com.googlecode.propidle.client.cache;

import com.googlecode.propidle.client.logging.Logger;
import com.googlecode.propidle.util.Exceptions;
import com.googlecode.totallylazy.Callable1;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.client.logging.Message.message;
import static com.googlecode.propidle.client.logging.PrintStreamLogger.printStreamLogger;
import static com.googlecode.totallylazy.Closeables.using;

public class CachingDynamicProperties {
    public static Callable<Properties> caching(final File cache, final Callable<Properties> callable, final Logger logger) {
        return new Callable<Properties>() {
            public Properties call() throws Exception {
                try {
                    return toCache(cache,callable.call());
                } catch (Exception e) {
                    logger.log(message(Exceptions.stackTraceToString(e)));
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

    public static Callable<Properties> caching(final File cache, final Callable<Properties> callable) {
        return caching(cache, callable, printStreamLogger(System.err));
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
