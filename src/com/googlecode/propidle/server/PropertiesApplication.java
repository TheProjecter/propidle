package com.googlecode.propidle.server;

import com.googlecode.propidle.WrapCallableInTransaction;
import com.googlecode.propidle.status.StatusModule;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.RestApplication;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import org.apache.lucene.store.Directory;

import java.util.Properties;
import java.util.concurrent.Callable;

public class PropertiesApplication extends RestApplication {
    public PropertiesApplication(Callable<Properties> propertyLoader, Directory directory, Iterable<Module> modules) {
        super();
        add(new StatusModule());
        add(new PropertiesModule(propertyLoader, directory));
        for (Module module : modules) {
            add(module);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T call(final Class<? extends Callable<T>> operation) throws Exception {
        return usingRequestScope(new Callable1<Container, T>() {
            public T call(Container container) throws Exception {
                container.add(Callable.class, operation);
                return (T) container.get(Callable.class).call();
            }
        });
    }
    
    public <T> T inTransaction(final Class<? extends Callable<T>> step) throws Exception {
        return usingRequestScope(new Callable1<Container, T>() {
            public T call(Container container) throws Exception {
                return inTransaction(container, step);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> T inTransaction(Container parent, Class<? extends Callable<T>> operation) throws Exception {
        Container container = new SimpleContainer(parent);
        container.add(Callable.class, operation);
        container.decorate(Callable.class, WrapCallableInTransaction.class);
        return (T) container.get(Callable.class).call();
    }
}
