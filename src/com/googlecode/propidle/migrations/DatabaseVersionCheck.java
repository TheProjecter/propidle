package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.log.MigrationLogFromRecords;
import com.googlecode.propidle.status.StatusCheck;
import com.googlecode.propidle.status.StatusCheckResult;
import com.googlecode.totallylazy.*;
import com.googlecode.yadic.Container;

import java.util.Properties;

import static com.googlecode.propidle.migrations.Migration.getMigrationNumber;
import static com.googlecode.propidle.migrations.log.MigrationLogFromRecords.databaseSchemaVersion;
import static com.googlecode.propidle.status.Action.action;
import static com.googlecode.propidle.status.ActionName.actionName;
import static com.googlecode.propidle.status.StatusCheckName.statusCheckName;
import static com.googlecode.propidle.status.StatusCheckResult.statusCheckResult;
import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Right.right;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.io.Url.url;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
import static com.googlecode.utterlyidle.proxy.Resource.urlOf;

public class DatabaseVersionCheck implements StatusCheck {
    private final Properties properties;
    public static final String ACTION_KEY = "Action";
    private static final String REQUIRED_VERSION = "Required version";
    private static final String ACTUAL_VERSION = "Actual version";

    public DatabaseVersionCheck(Properties properties) {
        this.properties = properties;
    }

    public StatusCheckResult check() throws Exception {
        Container container = MigrationsContainer.migrationsContainer(properties);
        StatusCheckResult result = statusCheckResult(
                statusCheckName(DatabaseVersionCheck.class.getSimpleName()));

        addRequiredSchemaVersion(container, result);
        result.add(ACTUAL_VERSION, actualSchemaVersion(container).value());
        addResult(result);

        return result;
    }

    private void addResult(StatusCheckResult result) throws Exception {
        Object requiredVersion = result.getProperty(REQUIRED_VERSION);
        Object actualVersion = result.getProperty(ACTUAL_VERSION);

        result.add(ACTION_KEY, requiredVersion == actualVersion ? "None required" : action(actionName("Migrate"), url(urlOf(resource(MigrationsResource.class).perform()))));
    }

    private void addRequiredSchemaVersion(Container container, StatusCheckResult result) {
        PropIdleMigrations migrations = container.get(PropIdleMigrations.class);
        Migration migration = sequence(migrations).sortBy(getMigrationNumber()).last();
        result.add(REQUIRED_VERSION, migration.number().value());
    }

    public static Either<Throwable, Integer> actualSchemaVersion(Container container) throws Exception {
        try {
            return right(databaseSchemaVersion(container.get(MigrationLogFromRecords.class)).value());
        } catch (LazyException e) {
            return left(Exceptions.getCause().call(e));
        }
    }

}
