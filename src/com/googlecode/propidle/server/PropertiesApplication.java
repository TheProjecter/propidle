package com.googlecode.propidle.server;

import com.googlecode.propidle.ApplicationPropertiesModule;
import com.googlecode.propidle.BasicModule;
import com.googlecode.propidle.WrapCallableInTransaction;
import com.googlecode.propidle.aliases.AliasesModule;
import com.googlecode.propidle.compositeproperties.CompositePropertiesModule;
import com.googlecode.propidle.diff.DiffModule;
import com.googlecode.propidle.filenames.FileNamesModule;
import com.googlecode.propidle.indexing.LuceneModule;
import com.googlecode.propidle.migrations.PropidleMigrationsModule;
import com.googlecode.propidle.monitoring.MonitoringModule;
import com.googlecode.propidle.root.RootModule;
import com.googlecode.propidle.search.SearchModule;
import com.googlecode.propidle.server.staticcontent.StaticContentModule;
import com.googlecode.propidle.status.StatusModule;
import com.googlecode.propidle.versioncontrol.changes.ChangesModule;
import com.googlecode.propidle.versioncontrol.revisions.RevisionsModule;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.RestApplication;
import com.googlecode.utterlyidle.migrations.modules.MigrationQueriesModule;
import com.googlecode.utterlyidle.migrations.modules.MigrationRegistrationModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import org.apache.lucene.store.Directory;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.migrations.SchemaVersionModule.schemaVersionModule;
import static com.googlecode.utterlyidle.migrations.util.Modules.asRequestScopeModule;

public class PropertiesApplication extends RestApplication {
    public PropertiesApplication(Callable<Properties> propertyLoader, Directory directory, Iterable<Module> modules) throws Exception {
        super();
        add(asRequestScopeModule().call(new MigrationQueriesModule()));

        add(new BasicModule());
        add(new LuceneModule(directory));
        add(new ApplicationPropertiesModule(propertyLoader));

        add(new AliasesModule());
        add(new CompositePropertiesModule());
        add(new DiffModule());
        add(new FileNamesModule());

        add(new MigrationRegistrationModule());
        add(new PropidleMigrationsModule());
        add(new MonitoringModule());
        add(schemaVersionModule());

        add(new PropertiesModule());
        add(new RootModule());
        add(new SearchModule());
        add(new StaticContentModule());
        add(new StatusModule());

        add(new ChangesModule());
        add(new RevisionsModule());
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
