package com.googlecode.propidle;

import com.googlecode.propidle.server.PropertiesApplication;
import com.googlecode.utterlyidle.modules.Module;
import org.apache.lucene.store.RAMDirectory;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.server.PersistenceModules.persistenceModules;
import static com.googlecode.propidle.util.TestRecords.hsqlConfiguration;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Sequences.sequence;

public class TestPropertiesApplication extends PropertiesApplication {
    public TestPropertiesApplication(Module... extraModules) throws Exception {
        this(sequence(extraModules));
    }

    private TestPropertiesApplication(Iterable<Module> extraModules) throws Exception {
        super(
                returns(hsqlConfiguration()),
                new RAMDirectory(),
                persistenceModules(properties("persistence=IN_MEMORY")).join(extraModules));
    }
}
