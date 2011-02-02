package com.googlecode.propidle.server;

import com.googlecode.propidle.persistence.PersistenceModule;
import com.googlecode.utterlyidle.RestApplication;
import org.apache.lucene.store.Directory;

public class PropertiesApplication extends RestApplication {
    public PropertiesApplication(Directory directory, PersistenceModule persistenceModule) {
        super();
        add(new PropertiesModule(directory));
        add(persistenceModule);
    }
}
