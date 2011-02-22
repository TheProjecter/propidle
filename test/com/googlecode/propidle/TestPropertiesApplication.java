package com.googlecode.propidle;

import com.googlecode.propidle.persistence.memory.InMemoryPersistenceModule;
import static com.googlecode.propidle.persistence.PropertiesBasedPersistence.persistenceStrategy;
import static com.googlecode.propidle.persistence.PropertiesBasedPersistence.Option.IN_MEMORY;
import com.googlecode.propidle.persistence.PropertiesBasedPersistence;
import com.googlecode.propidle.server.PropertiesApplication;
import static com.googlecode.propidle.util.TestRecords.inMemoryDatabaseConfiguraton;
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
                returns(inMemoryDatabaseConfiguraton()),
                new RAMDirectory(),
                persistenceStrategy(IN_MEMORY).join(extraModules));
    }

}
