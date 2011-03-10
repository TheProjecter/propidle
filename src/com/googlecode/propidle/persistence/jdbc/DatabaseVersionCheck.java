package com.googlecode.propidle.persistence.jdbc;

import com.googlecode.propidle.migrations.Migration;
import com.googlecode.propidle.migrations.Migrations;
import com.googlecode.propidle.migrations.MigrationResource;
import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.propidle.status.StatusCheck;
import com.googlecode.propidle.status.StatusCheckResult;
import com.googlecode.totallylazy.*;

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
    public static final String ACTION_KEY = "Action";
    private static final String REQUIRED_VERSION = "Required version";
    private static final String ACTUAL_VERSION = "Actual version";

    private final Migrations migrations;
    private final MigrationLog migrationLog;

    public DatabaseVersionCheck(Migrations migrations, MigrationLog migrationLog) {
        this.migrations = migrations;
        this.migrationLog = migrationLog;
    }

    public StatusCheckResult check() throws Exception {
        StatusCheckResult result = statusCheckResult(
                statusCheckName(DatabaseVersionCheck.class.getSimpleName()));

        addRequiredSchemaVersion(result);
        result.add(ACTUAL_VERSION, actualSchemaVersion(migrationLog).value());
        addResult(result);

        return result;
    }

    private void addResult(StatusCheckResult result) throws Exception {
        Object requiredVersion = result.getProperty(REQUIRED_VERSION);
        Object actualVersion = result.getProperty(ACTUAL_VERSION);

        result.add(ACTION_KEY, requiredVersion == actualVersion ? "None required" : action(actionName("Migrate"), url(urlOf(resource(MigrationResource.class).perform()))));
    }

    private void addRequiredSchemaVersion(StatusCheckResult result) {
        Migration migration = sequence(migrations).sortBy(getMigrationNumber()).last();
        result.add(REQUIRED_VERSION, migration.number().value());
    }

    public static Either<Throwable, Integer> actualSchemaVersion(MigrationLog migrationLog) throws Exception {
        try {
            return right(databaseSchemaVersion(migrationLog).value());
        } catch (LazyException e) {
            return left(Exceptions.getCause().call(e));
        }
    }

}
