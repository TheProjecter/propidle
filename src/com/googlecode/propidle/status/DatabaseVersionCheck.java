package com.googlecode.propidle.status;

import com.googlecode.propidle.migrations.MigrationResource;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Exceptions;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.utterlyidle.migrations.Migration;
import com.googlecode.utterlyidle.migrations.ModuleMigrationsCollector;
import com.googlecode.utterlyidle.migrations.log.MigrationLog;

import static com.googlecode.propidle.status.Action.action;
import static com.googlecode.propidle.status.ActionName.actionName;
import static com.googlecode.propidle.status.StatusCheckName.statusCheckName;
import static com.googlecode.propidle.status.StatusCheckResult.statusCheckResult;
import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Right.right;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.io.Url.url;
import static com.googlecode.utterlyidle.migrations.log.MigrationLogFromRecords.databaseSchemaVersion;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
import static com.googlecode.utterlyidle.proxy.Resource.urlOf;

public class DatabaseVersionCheck implements StatusCheck {
    public static final String ACTION_KEY = "Action";
    private static final String REQUIRED_VERSION = "Required version";
    private static final String ACTUAL_VERSION = "Actual version";

    private final ModuleMigrationsCollector moduleMigrationsCollector;
    private final MigrationLog migrationLog;

    public DatabaseVersionCheck(ModuleMigrationsCollector moduleMigrationsCollector, MigrationLog migrationLog) {
        this.moduleMigrationsCollector = moduleMigrationsCollector;
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
        Migration migration = sequence(moduleMigrationsCollector.moduleMigrations().first().migrations()).sortBy(migrationNumber()).last();
        result.add(REQUIRED_VERSION, migration.number().value());
    }

    private Callable1<Migration, Comparable> migrationNumber() {
        return new Callable1<Migration, Comparable>() {
            public Comparable call(Migration migration) throws Exception {
                return migration.number();
            }
        };
    }

    public static Either<Throwable, Integer> actualSchemaVersion(MigrationLog migrationLog) throws Exception {
        try {
            return right(databaseSchemaVersion(migrationLog).value());
        } catch (LazyException e) {
            return left(Exceptions.getCause().call(e));
        }
    }

}
