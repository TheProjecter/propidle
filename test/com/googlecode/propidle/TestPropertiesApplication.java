package com.googlecode.propidle;

import static com.googlecode.propidle.PersistenceMechanism.IN_MEMORY;
import com.googlecode.propidle.server.PersistenceModules;
import com.googlecode.propidle.server.PropertiesApplication;
import static com.googlecode.propidle.server.PersistenceModules.persistenceModules;
import static com.googlecode.propidle.util.TestRecords.hsqlConfiguraton;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.utterlyidle.modules.Module;
import org.apache.lucene.store.RAMDirectory;

public class TestPropertiesApplication extends PropertiesApplication {
    public TestPropertiesApplication(Module... extraModules) throws Exception {
        this(sequence(extraModules));
    }
    public TestPropertiesApplication(Iterable<Module> extraModules) throws Exception {
        super(
                returns(hsqlConfiguraton()),
                new RAMDirectory(),
                persistenceModules(properties("persistence=in_memory")).join(extraModules));
    }
}
