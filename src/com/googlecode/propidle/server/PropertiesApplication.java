package com.googlecode.propidle.server;

import com.googlecode.propidle.ApplicationPropertiesModule;
import com.googlecode.propidle.BasicModule;
import com.googlecode.propidle.PropertyTriggeredExecutor;
import com.googlecode.propidle.WrapCallableInTransaction;
import com.googlecode.propidle.aliases.AliasesModule;
import com.googlecode.propidle.compositeproperties.CompositePropertiesModule;
import com.googlecode.propidle.diff.DiffModule;
import com.googlecode.propidle.exceptions.ExceptionFormattingModule;
import com.googlecode.propidle.filenames.FileNamesModule;
import com.googlecode.propidle.indexing.LuceneModule;
import com.googlecode.propidle.migrations.PropidleMigrationsModule;
import com.googlecode.propidle.monitoring.MonitoringModule;
import com.googlecode.propidle.navigation.NavigationModule;
import com.googlecode.propidle.properties.PropertyValue;
import com.googlecode.propidle.root.RootModule;
import com.googlecode.propidle.scheduling.ScheduleTask;
import com.googlecode.propidle.scheduling.SchedulingModule;
import com.googlecode.propidle.search.SearchModule;
import com.googlecode.propidle.server.sitemesh.SiteMashDecoratorModule;
import com.googlecode.propidle.server.sitemesh.SiteMashHandlerModule;
import com.googlecode.propidle.server.staticcontent.StaticContentModule;
import com.googlecode.propidle.status.StatusModule;
import com.googlecode.propidle.versioncontrol.changes.ChangesModule;
import com.googlecode.propidle.versioncontrol.revisions.RevisionsModule;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.RestApplication;
import com.googlecode.utterlyidle.migrations.modules.MigrationQueriesModule;
import com.googlecode.utterlyidle.migrations.modules.MigrationRegistrationModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import org.apache.lucene.store.Directory;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.ApplicationPropertiesModule.RELOAD_PROPERTIES_TASK_NAME;
import static com.googlecode.propidle.indexing.LuceneModule.REBUILD_INDEX_TASK_NAME;
import static com.googlecode.propidle.migrations.SchemaVersionModule.schemaVersionModule;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.utterlyidle.migrations.util.Modules.asRequestScopeModule;
import static java.lang.Long.valueOf;

public class PropertiesApplication extends RestApplication {

    public PropertiesApplication(Callable<Properties> propertyLoader, Directory directory, Iterable<Module> modules, BasePath basePath) throws Exception {
        super(basePath);

        add(asRequestScopeModule().call(new MigrationQueriesModule()));

        add(new SchedulingModule());

        add(new ApplicationPropertiesModule(propertyLoader));
        add(new BasicModule());
        add(new LuceneModule(directory));

        add(new AliasesModule());
        add(new NavigationModule());
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

        add(new SiteMashDecoratorModule());

        for (Module module : modules) {
            add(module);
        }

        add(new ExceptionFormattingModule());
        add(new SiteMashHandlerModule());
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

    public void startPropertyDependentTasks() {
        PropertyTriggeredExecutor executor = applicationScope().get(PropertyTriggeredExecutor.class);
        executor.register(propertyName("lucene.index.refresh.time.in.seconds"), scheduleIndexRebuild(applicationScope().get(ScheduleTask.class)), propertyValue("60"));
        executor.register(propertyName("properties.refresh.time.in.seconds"), reloadProperties(applicationScope().get(ScheduleTask.class)), propertyValue("60"));
    }

    public Callable1<PropertyValue, Void> reloadProperties(final ScheduleTask scheduleTaskRequest) {
        return new Callable1<PropertyValue, Void>() {
            public Void call(PropertyValue propertyValue) throws Exception {
                scheduleTaskRequest.schedule(RELOAD_PROPERTIES_TASK_NAME, valueOf(propertyValue.value()), valueOf(propertyValue.value()));
                return null;
            }
        };
    }

    private Callable1<PropertyValue, Void> scheduleIndexRebuild(final ScheduleTask scheduleTaskRequest) {
        return new Callable1<PropertyValue, Void>() {
            public Void call(PropertyValue propertyValue) throws Exception {
                scheduleTaskRequest.schedule(REBUILD_INDEX_TASK_NAME, valueOf(propertyValue.value()));
                return null;
            }
        };
    }

}