package com.googlecode.propidle;

import com.googlecode.propidle.persistence.memory.InMemoryPersistenceModule;
import com.googlecode.propidle.server.PropertiesApplication;
import com.googlecode.utterlyidle.modules.Module;
import org.apache.lucene.store.RAMDirectory;

public class TestPropertiesApplication extends PropertiesApplication {
    public TestPropertiesApplication(Module... extraModules) throws Exception {
        super(
//                TemporaryIndex.directory(new File("/Users/mattsavage/Desktop/lucene")),
new RAMDirectory(),
new InMemoryPersistenceModule());
//                new SqlPersistenceModule(connectionDetails("jdbc:hsqldb:mem:" + UUID.randomUUID(), "SA", "")));
        for (Module extraModule : extraModules) {
            add(extraModule);
        }
    }

}
