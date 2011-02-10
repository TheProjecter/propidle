package com.googlecode.propidle.server;

import com.googlecode.propidle.persistence.PersistenceModule;
import com.googlecode.propidle.WrapCallableInTransaction;
import com.googlecode.utterlyidle.RestApplication;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import org.apache.lucene.store.Directory;

import java.util.concurrent.Callable;

public class PropertiesApplication extends RestApplication {
    public PropertiesApplication(Directory directory, PersistenceModule persistenceModule) {
        super();
        add(new PropertiesModule(directory));
        add(persistenceModule);
    }

    @SuppressWarnings("unchecked")
    public <T> T inTransaction(Class<? extends Callable<T>> step) throws Exception {
        Container request = new SimpleContainer(createRequestScope());
        request.add(Callable.class, step);
        request.decorate(Callable.class, WrapCallableInTransaction.class);
        return (T) request.get(Callable.class).call();
    }
}
