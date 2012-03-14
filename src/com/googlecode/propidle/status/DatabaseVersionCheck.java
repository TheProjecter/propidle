package com.googlecode.propidle.status;

import com.googlecode.propidle.migrations.MigrationResource;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Exceptions;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.Redirector;
import com.googlecode.utterlyidle.migrations.Migration;
import com.googlecode.utterlyidle.migrations.ModuleMigrations;
import com.googlecode.utterlyidle.migrations.ModuleMigrationsCollector;
import com.googlecode.utterlyidle.migrations.ModuleName;
import com.googlecode.utterlyidle.migrations.log.MigrationLog;
import com.googlecode.yadic.Resolver;

import static com.googlecode.propidle.status.Action.action;
import static com.googlecode.propidle.status.ActionName.actionName;
import static com.googlecode.propidle.status.StatusCheckName.statusCheckName;
import static com.googlecode.propidle.status.StatusCheckResult.statusCheckResult;
import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Right.right;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Uri.uri;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.migrations.MigrationNumbers.databaseSchemaVersion;
import static java.lang.String.format;

public class DatabaseVersionCheck implements StatusCheck {
    public static final String ACTION_KEY = "Action";
    public static final String REQUIRED_VERSION = "Required version";
    public static final String ACTUAL_VERSION = "Actual version";

    private final ModuleMigrationsCollector moduleMigrationsCollector;
    private final MigrationLog migrationLog;
    private final Resolver resolver;
    private final Redirector redirector;

    public DatabaseVersionCheck(ModuleMigrationsCollector moduleMigrationsCollector, MigrationLog migrationLog, Resolver resolver, Redirector redirector) {
        this.moduleMigrationsCollector = moduleMigrationsCollector;
        this.migrationLog = migrationLog;
        this.resolver = resolver;
        this.redirector = redirector;
    }

    public StatusCheckResult check() throws Exception {
        StatusCheckResult result = statusCheckResult(
                statusCheckName(DatabaseVersionCheck.class.getSimpleName()));

        Sequence<ModuleMigrations> allMigrations = moduleMigrationsCollector.moduleMigrations(resolver);
        boolean migrationRequired = false;
        for (ModuleMigrations moduleMigrations : allMigrations) {
            Integer required = addRequiredSchemaVersion(result, moduleMigrations);
            Either<Throwable, Integer> actual = actualSchemaVersion(moduleMigrations.moduleName(), migrationLog);
            if (actual.isLeft() || !required.equals(actual.right())) {
                migrationRequired = true;
            }
            result.add(format("%s : %s", moduleMigrations.moduleName(), ACTUAL_VERSION), actual.value());
        }
        addResult(migrationRequired, result);

        if (migrationRequired) {
            result.setFatal(true);
        }

        return result;
    }

    private void addResult(boolean migrationRequired, StatusCheckResult result) throws Exception {
        result.add(ACTION_KEY, migrationRequired ? action(actionName("Migrate"), uri("/" + redirector.resourceUriOf(method(on(MigrationResource.class).perform())))) : "None required");
    }

    private Integer addRequiredSchemaVersion(StatusCheckResult result, final ModuleMigrations moduleMigrations) {
        Sequence<Migration> requiredMigrations = sequence(moduleMigrations.migrations()).sortBy(migrationNumber());
        Integer value = requiredMigrations.isEmpty() ? 0 : requiredMigrations.last().number().value();
        result.add(format("%s : %s", moduleMigrations.moduleName(), REQUIRED_VERSION), value);
        return value;
    }

    private Callable1<Migration, Comparable> migrationNumber() {
        return new Callable1<Migration, Comparable>() {
            public Comparable call(Migration migration) throws Exception {
                return migration.number();
            }
        };
    }

    public static Either<Throwable, Integer> actualSchemaVersion(ModuleName moduleName, MigrationLog migrationLog) throws Exception {
        try {
            return right(databaseSchemaVersion(moduleName, migrationLog).value());
        } catch (LazyException e) {
            return left(Exceptions.getCause().call(e));
        }
    }

}
