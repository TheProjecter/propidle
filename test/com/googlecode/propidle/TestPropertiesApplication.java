package com.googlecode.propidle;

import com.googlecode.propidle.persistence.memory.InMemoryPersistenceModule;
import com.googlecode.propidle.server.PropertiesApplication;
import static com.googlecode.propidle.util.TestRecords.inMemoryDatabaseConfiguraton;
import static com.googlecode.totallylazy.Callables.returns;
import com.googlecode.utterlyidle.modules.Module;
import org.apache.lucene.store.RAMDirectory;

public class TestPropertiesApplication extends PropertiesApplication {
    public TestPropertiesApplication(Module... extraModules) throws Exception {
        super(
                returns(inMemoryDatabaseConfiguraton()),
                new RAMDirectory(),
                new InMemoryPersistenceModule(),
                extraModules);
    }

}
