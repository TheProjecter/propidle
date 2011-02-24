package com.googlecode.propidle;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.server.PersistenceModules.persistenceModules;
import com.googlecode.propidle.server.PropertiesApplication;
import static com.googlecode.propidle.util.TestRecords.hsqlConfiguraton;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.utterlyidle.modules.Module;
import org.apache.lucene.store.RAMDirectory;

public class TestPropertiesApplication extends PropertiesApplication {
    public TestPropertiesApplication(Module... extraModules) {
        this(sequence(extraModules));
    }

    public TestPropertiesApplication(Iterable<Module> extraModules) {
        super(
                returns(hsqlConfiguraton()),
                new RAMDirectory(),
                persistenceModules(properties("persistence=in_memory")).join(extraModules));
    }
}
