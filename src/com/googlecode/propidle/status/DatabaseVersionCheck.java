package com.googlecode.propidle.status;

import com.googlecode.propidle.migrations.Migration;
import com.googlecode.propidle.migrations.MigrationNumber;
import com.googlecode.propidle.migrations.MigrationsContainer;
import com.googlecode.propidle.migrations.PropIdleMigrations;
import com.googlecode.propidle.migrations.log.MigrationLogFromRecords;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import com.googlecode.utterlyidle.io.Url;
import com.googlecode.yadic.Container;

import java.util.Properties;

import static com.googlecode.propidle.migrations.Migration.getMigrationNumber;
import static com.googlecode.propidle.status.StatusCheckName.statusCheckName;
import static com.googlecode.propidle.status.StatusCheckResult.statusCheckResult;
import static com.googlecode.totallylazy.Sequences.sequence;

public class DatabaseVersionCheck implements StatusCheck {
    private final Properties properties;
    public static final String RESULT_KEY = "Result";

    public DatabaseVersionCheck(Properties properties) {
        this.properties = properties;
    }

    public StatusCheckResult check() throws Exception {
        Container container = MigrationsContainer.migrationsContainer(properties);
        PropIdleMigrations migrations = container.get(PropIdleMigrations.class);
        Migration migration = sequence(migrations).sortBy(getMigrationNumber()).last();
        MigrationLogFromRecords migrationLog = container.get(MigrationLogFromRecords.class);
        MigrationNumber databaseSchemaVersion = sequence(migrationLog.list()).sortBy(MigrationLogItem.getMigrationNumber()).last().number();

        Integer requiredVersion = migration.number().value();
        Integer actualVersion = databaseSchemaVersion.value();
        StatusCheckResult result = statusCheckResult(
                statusCheckName(DatabaseVersionCheck.class.getSimpleName()))
                .add("Required version", requiredVersion)
                .add("Actual version", actualVersion)
                .add(RESULT_KEY, isCurrentVersion(requiredVersion, actualVersion) ? "PASS" : Url.url("migrate/perform"));


        return result;
    }

    private boolean isCurrentVersion(Integer requiredVersion, Integer actualVersion) {
        return requiredVersion == actualVersion;

    }
}
