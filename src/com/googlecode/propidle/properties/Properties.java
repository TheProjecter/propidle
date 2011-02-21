package com.googlecode.propidle.properties;

import com.googlecode.propidle.util.tinytype.TinyType;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import static java.lang.String.format;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Properties {
    public static java.util.Properties properties(InputStream stream) throws IOException {
        java.util.Properties properties = new java.util.Properties();
        try {
            properties.load(stream);
        } finally {
            stream.close();
        }
        return properties;
    }

    public static java.util.Properties properties(TinyType<String,?> text) {
        return properties(text.value());
    }

    public static java.util.Properties properties(String values) {
        java.util.Properties properties = new java.util.Properties();
        try {
            properties.load(new ByteArrayInputStream(values.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("This should be impossible", e);
        }
        return properties;
    }

    public static java.util.Properties properties(Pair<String, String>... values) {
        return properties(sequence(values));
    }

    public static java.util.Properties properties(Iterable<Pair<String, String>> values) {
        java.util.Properties properties = new java.util.Properties();
        for (Pair<String, String> value : values) {
            properties.setProperty(value.first(), value.second());
        }
        return properties;
    }

    public static java.util.Properties properties(Map properties) {
        return properties(toPairs(properties));
    }

    @SuppressWarnings("unchecked")
    public static Sequence<Pair<String, String>> toPairs(Map properties) {
        return sequence(properties.entrySet()).map(toPair());
    }

    private static Callable1<? super java.util.Properties, Iterable<? extends Map.Entry<Object, Object>>> entrySets() {
        return new Callable1<java.util.Properties, Iterable<? extends Map.Entry<Object, Object>>>() {
            public Iterable<? extends Map.Entry<Object, Object>> call(java.util.Properties properties) throws Exception {
                return properties.entrySet();
            }
        };
    }

    public static Callable1<? super Map.Entry<Object, Object>, Pair<String, String>> toPair() {
        return new Callable1<Map.Entry<Object, Object>, Pair<String, String>>() {
            public Pair<String, String> call(Map.Entry<Object, Object> entry) throws Exception {
                return pair((String) entry.getKey(), (String) entry.getValue());
            }
        };
    }

    public static Callable2<? super java.util.Properties, ? super Map.Entry<Object, Object>, java.util.Properties> setProperty() {
        return new Callable2<java.util.Properties, Map.Entry<Object, Object>, java.util.Properties>() {
            public java.util.Properties call(java.util.Properties properties, Map.Entry<Object, Object> property) throws Exception {
                properties.put(property.getKey(), property.getValue());
                return properties;
            }
        };
    }

    public static java.util.Properties compose(java.util.Properties... properties) {
        return compose(sequence(properties));
    }

    public static java.util.Properties compose(Iterable<java.util.Properties> propertiesSequence) {
        return sequence(propertiesSequence).flatMap(entrySets()).fold(new java.util.Properties(), setProperty());
    }

    public static Callable2<? super java.util.Properties, ? super java.util.Properties, java.util.Properties> compose() {
        return new Callable2<java.util.Properties, java.util.Properties, java.util.Properties>() {
            public java.util.Properties call(java.util.Properties soFar, java.util.Properties nextProperties) throws Exception {
                return compose(soFar, nextProperties);
            }
        };
    }

    public static String asString(java.util.Properties properties) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            properties.store(out, "");
        } catch (IOException e) {
            throw new RuntimeException("Problem writing to ByteArrayOutputStream. This should never happen", e);
        }
        return out.toString();
    }

    public static Callable1<? super Map.Entry<Object, Object>, ? extends Comparable> key() {
        return new Callable1<Map.Entry<Object, Object>, Comparable>() {
            public Comparable call(Map.Entry<Object, Object> entry) throws Exception {
                return (String) entry.getKey();
            }
        };
    }

    public static Callable1<? super String, java.util.Properties> propertiesFromString() {
        return new Callable1<String, java.util.Properties>() {
            public java.util.Properties call(String text) throws Exception {
                return properties(text);
            }
        };
    }

    public static String getOrFail(java.util.Properties properties, String name) {
        if(!properties.containsKey(name)){
            throw new RuntimeException(format("Expected property '%s' to be defined", name));
        }
        return properties.getProperty(name);
    }
}
