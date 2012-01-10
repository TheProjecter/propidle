package com.googlecode.propidle;

import com.googlecode.propidle.server.PropertiesApplication;
import com.googlecode.propidle.server.Server;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.modules.Module;
import org.apache.lucene.store.RAMDirectory;

import java.util.Properties;

import static com.googlecode.propidle.PersistenceMechanism.HSQL;
import static com.googlecode.propidle.PersistenceMechanism.ORACLE;
import static com.googlecode.propidle.PersistenceMechanism.PERSISTENCE;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.server.PersistenceModules.persistenceModules;
import static com.googlecode.propidle.util.TestRecords.hsqlConfiguration;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.util.UUID.randomUUID;

public class TestPropertiesApplication extends PropertiesApplication {
    public TestPropertiesApplication(Module... extraModules) throws Exception {
        this(sequence(extraModules));

    }

    private TestPropertiesApplication(Iterable<Module> extraModules) throws Exception {
        super(
                returns(testProperties()),
                new RAMDirectory(),
                persistenceModules(testProperties()).join(extraModules), BasePath.basePath("/"));
    }

    private static Properties testProperties() {
        return properties("persistence=in_memory");
    }
}
