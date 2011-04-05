package com.googlecode.propidle.status;

import com.googlecode.totallylazy.None;
import com.googlecode.totallylazy.Option;
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
    private ModuleMigrationsCollector migrationsCollector = new ModuleMigrationsCollector(new MissingResolver());


    @Test
    public void statesThatVersionsAreZeroWhenThereAreNone() throws Exception {
        migrationsCollector.add(ModuleMigrationsWithNoMigrations.class);

        StatusCheckResult result = new DatabaseVersionCheck(migrationsCollector, emptyMigrationLog()).check();

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
