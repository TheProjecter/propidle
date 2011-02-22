package com.googlecode.propidle.server;

import static com.googlecode.propidle.util.TestRecords.inMemoryDatabaseConfiguraton;
import static com.googlecode.totallylazy.Sequences.empty;
import com.googlecode.utterlyidle.modules.Module;

import java.util.Properties;

public class TestServer extends Server {
    public static void main(String[] args) throws Exception {
        new TestServer(8000);
    }

    public TestServer(int port) throws Exception {
        super(propertiesFor(port), empty(Module.class));
    }

    private static Properties propertiesFor(int port) {
        Properties properties = inMemoryDatabaseConfiguraton();
        properties.setProperty(PORT, String.valueOf(port));
        return properties;
    }
}
