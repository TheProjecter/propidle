package com.googlecode.propidle.server;

import com.googlecode.propidle.persistence.PersistenceModule;
import com.googlecode.propidle.WrapCallableInTransaction;
import com.googlecode.utterlyidle.RestApplication;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import org.apache.lucene.store.Directory;

import java.util.concurrent.Callable;

public class PropertiesApplication extends RestApplication {
    public PropertiesApplication(Directory directory, PersistenceModule persistenceModule, Module... extraModules) {
        super();
        add(new PropertiesModule(directory));
        add(persistenceModule);
        for (Module extraModule : extraModules) {
            add(extraModule);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T inTransaction(Class<? extends Callable<T>> step) throws Exception {
        return inTransaction(createRequestScope(), step);
    }

    @SuppressWarnings("unchecked")
    public static <T> T inTransaction(Container parent, Class<? extends Callable<T>> operation) throws Exception {
        Container container = new SimpleContainer(parent);
        container.add(Callable.class, operation);
        container.decorate(Callable.class, WrapCallableInTransaction.class);
        return (T) container.get(Callable.class).call();
    }
}
