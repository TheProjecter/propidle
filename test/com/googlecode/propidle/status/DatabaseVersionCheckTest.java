package com.googlecode.propidle.status;

import com.googlecode.totallylazy.None;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Uri;
import com.googlecode.totallylazy.proxy.Invocation;
import com.googlecode.utterlyidle.BaseUri;
import com.googlecode.utterlyidle.BaseUriRedirector;
import com.googlecode.utterlyidle.Binding;
import com.googlecode.utterlyidle.Redirector;
import com.googlecode.utterlyidle.RegisteredResources;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.migrations.*;
import com.googlecode.utterlyidle.migrations.log.MigrationLog;
import com.googlecode.utterlyidle.migrations.log.MigrationLogItem;
import com.googlecode.yadic.resolvers.MissingResolver;
import org.junit.Test;

import static com.googlecode.propidle.status.DatabaseVersionCheck.ACTUAL_VERSION;
import static com.googlecode.propidle.status.DatabaseVersionCheck.REQUIRED_VERSION;
import static com.googlecode.totallylazy.Sequences.empty;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DatabaseVersionCheckTest {
    public static final BaseUriRedirector EMPTY_REDIRECTOR = new BaseUriRedirector(BaseUri.baseUri("/"), new RegisteredResources());
    private ModuleMigrationsCollector migrationsCollector = new ModuleMigrationsCollector();


    @Test
    public void statesThatVersionsAreZeroWhenThereAreNone() throws Exception {
        migrationsCollector.add(ModuleMigrationsWithNoMigrations.class);

        StatusCheckResult result = new DatabaseVersionCheck(migrationsCollector, emptyMigrationLog(), new MissingResolver(), EMPTY_REDIRECTOR).check();

        assertThat(result.getProperty(requiredVersion()).toString(), is(equalTo("0")));
        assertThat(result.getProperty(actualVersion()).toString(), is(equalTo("0")));
    }

    private String actualVersion() {
        return format("%s : %s", ModuleMigrationsWithNoMigrations.moduleName, ACTUAL_VERSION);
    }

    private String requiredVersion() {
        return format("%s : %s", ModuleMigrationsWithNoMigrations.moduleName, REQUIRED_VERSION);
    }

    private MigrationLog emptyMigrationLog() {
        return new MigrationLog() {
            public Option<MigrationLogItem> get(MigrationNumber migrationNumber, ModuleName moduleName) {
                return None.none();
            }

            public Iterable<MigrationLogItem> add(Iterable<MigrationLogItem> auditItems) {
                throw new UnsupportedOperationException();
            }

            public Iterable<MigrationLogItem> list() {
                return empty(MigrationLogItem.class);
            }
        };
    }

    public static class ModuleMigrationsWithNoMigrations implements ModuleMigrations {
        public static ModuleName moduleName = ModuleName.moduleName("Test Module");

        public ModuleName moduleName() {
            return moduleName;
        }

        public Migrations migrations() {
            return Migrations.migrations(empty(Migration.class));
        }
    }
}
