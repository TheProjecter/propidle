package com.googlecode.propidle;

import com.googlecode.propidle.server.PropertiesApplication;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.modules.Module;
import org.apache.lucene.store.RAMDirectory;

import java.util.Properties;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.server.PersistenceModules.persistenceModules;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.BasePath.basePath;

public class TestPropertiesApplication extends PropertiesApplication {
    public static BasePath basePath = basePath("/");

    public TestPropertiesApplication(Module... extraModules) throws Exception {
        this(sequence(extraModules));

    }

    public TestPropertiesApplication(Properties properties, Module... extraModules) throws Exception {
        super(
                returns(properties),
                new RAMDirectory(),
                persistenceModules(properties).join(sequence(extraModules)), basePath);

    }

    private TestPropertiesApplication(Iterable<Module> extraModules) throws Exception {
        super(
                returns(testProperties()),
                new RAMDirectory(),
                persistenceModules(testProperties()).join(extraModules), basePath);
    }

    private static Properties testProperties() {
        return properties("persistence=in_memory");
    }
}
