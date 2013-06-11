package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.bootstrap.hsql.HsqlMigrationsModule;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import com.googlecode.propidle.migrations.modules.MigrationActionsModule;
import com.googlecode.propidle.migrations.modules.MigrationQueriesModule;
import com.googlecode.propidle.migrations.modules.MigrationRegistrationModule;
import com.googlecode.propidle.migrations.persistence.jdbc.SqlPersistenceModule;
import com.googlecode.propidle.migrations.persistence.jdbc.hsql.HsqlModule;
import com.googlecode.propidle.migrations.pretend.module1.Module1Migrations;
import com.googlecode.propidle.migrations.pretend.module2.Module2Migrations;
import com.googlecode.propidle.migrations.util.WrapCallableInTransaction;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.time.Clock;
import com.googlecode.totallylazy.time.SystemClock;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.migrations.persistence.jdbc.ConnectionDetails.connectionDetails;
import static com.googlecode.propidle.migrations.util.Modules.asRequestScopeModule;
import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.modules.Modules.activate;
import static com.googlecode.yadic.Containers.selfRegister;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MigrationsTest {

    @Test
    public void testMigrations() throws Exception {
        TestApplication application = new TestApplication();
        application.add(asRequestScopeModule().call(new MigrationActionsModule()));
        application.add(asRequestScopeModule().call(new MigrationQueriesModule()));
        application.add(new MigrationRegistrationModule());
        application.add(asRequestScopeModule().call(new HsqlMigrationsModule()));
        application.add(new HsqlModule());
        application.add(new SqlPersistenceModule(connectionDetails("jdbc:hsqldb:mem:HsqlCreateMigrationLogTableTest", "SA", "")));
        application.add(new TestMigrationsModule());
        Sequence<MigrationLogItem> logItems = sequence(application.inTransaction(RunMigrations.class));
        assertThat(logItems.first().moduleName(), is(equalTo(ModuleName.moduleName("Module1"))));
        assertThat(logItems.first().name(), is(equalTo(MigrationName.migrationName("create-two-mahogany-tables"))));

        assertThat(logItems.second().moduleName(), is(equalTo(ModuleName.moduleName("Module1"))));
        assertThat(logItems.second().name(), is(equalTo(MigrationName.migrationName("create-a-plastic-table"))));

        assertThat(logItems.last().moduleName(), is(equalTo(ModuleName.moduleName("Module2"))));
        assertThat(logItems.last().name(), is(equalTo(MigrationName.migrationName("create-a-coffee-table"))));
    }

    public static class TestApplication implements Application {
        private final Container applicationScope = selfRegister(new SimpleContainer());
        private final List<Module> modules = new ArrayList<Module>();

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

        public Container applicationScope() {
            return applicationScope;
        }

        private Container createRequestScope() {
            final Container requestScope = selfRegister(new SimpleContainer(applicationScope));
            sequence(modules).forEach(activate(requestScope, Sequences.<Class<? extends Module>>sequence(RequestScopedModule.class)));
            return requestScope;
        }


        public <T> T usingRequestScope(Callable1<Container, T> callable) {
            return using(createRequestScope(), callable);
        }

        @Override
        public <T> T usingArgumentScope(Request request, Callable1<Container, T> containerTCallable1) {
            throw new UnsupportedOperationException("not done yet");
        }

        public <T> T usingParameterScope(Request request, Callable1<Container, T> containerTCallable1) {
            throw new UnsupportedOperationException("not implemented");
        }

        public Application add(Module module) {
            sequence(modules).forEach(activate(applicationScope, Sequences.<Class<? extends Module>>sequence(ApplicationScopedModule.class)));
            modules.add(module);
            return this;
        }

        public void close() throws IOException {

        }

        public Response handle(Request request) throws Exception {
            return null;
        }

        public void start() throws Exception {
            throw new RuntimeException("Not done yet");
        }

        public void stop() throws Exception {
            throw new RuntimeException("Not done yet");
        }
    }

    public static class TestMigrationsModule implements RequestScopedModule, ApplicationScopedModule {
        public Container addPerRequestObjects(Container container) {
            container.get(ModuleMigrationsCollector.class).add(Module1Migrations.class);
            container.get(ModuleMigrationsCollector.class).add(Module2Migrations.class);
            container.add(Clock.class, SystemClock.class);
            return container;
        }

        public Container addPerApplicationObjects(Container container) throws Exception {
            return container.addInstance(PrintStream.class, System.out);
        }
    }
}
