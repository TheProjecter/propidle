package com.googlecode.propidle.migrations.sql;

import static com.googlecode.propidle.util.reflection.Packages.filesInSamePackageAs;
import static com.googlecode.propidle.migrations.sql.SqlMigration.sqlMigration;
import static com.googlecode.propidle.migrations.sql.SqlMigration.parseMigrationFileId;
import static com.googlecode.propidle.migrations.Migration.migration;
import com.googlecode.propidle.migrations.Migration;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Callable1;
import static com.googlecode.totallylazy.Sequences.sequence;

import java.net.URL;

public class SqlMigrations {

    public static Iterable<Migration> sqlMigrationsInSamePackageAs(Class aClass, SqlExecutor executor) {
        return migrationFilesFromUrls(executor, sequence(filesInSamePackageAs(aClass)).filter(sqlFiles()));
    }

    public static Iterable<Migration> migrationFilesFromUrls(SqlExecutor executor, Iterable<URL> urls) {
        return sequence(urls).map(migrationUsing(executor));
    }

    private static Callable1<? super URL, Migration> migrationUsing(final SqlExecutor executor) {
        return new Callable1<URL, Migration>() {
            public Migration call(URL url) throws Exception {
                return migration(parseMigrationFileId(url), sqlMigration(url, executor));
            }
        };
    }

    private static Predicate<? super URL> sqlFiles() {
        return new Predicate<URL>() {
            public boolean matches(URL url) {
                return url.getFile().endsWith(".sql");
            }
        };
    }
}
