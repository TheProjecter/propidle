package com.googlecode.propidle.migrations.sql;

import com.googlecode.propidle.migrations.Migration;
import static com.googlecode.propidle.migrations.Migration.migration;
import static com.googlecode.propidle.migrations.sql.SqlMigration.parseMigrationFileId;
import static com.googlecode.propidle.migrations.sql.SqlMigration.sqlMigration;
import static com.googlecode.propidle.util.reflection.Packages.filesInSamePackageAs;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.totallylazy.records.sql.SqlRecords;

import java.net.URL;

public class SqlMigrations {

    public static Iterable<Migration> sqlMigrationsInSamePackageAs(Class aClass, SqlRecords records) {
        return migrationFilesFromUrls(records, sequence(filesInSamePackageAs(aClass)).filter(sqlFiles()));
    }

    public static Iterable<Migration> migrationFilesFromUrls(SqlRecords records, Iterable<URL> urls) {
        return sequence(urls).map(migrationUsing(records));
    }

    private static Callable1<? super URL, Migration> migrationUsing(final SqlRecords records) {
        return new Callable1<URL, Migration>() {
            public Migration call(URL url) throws Exception {
                return migration(parseMigrationFileId(url), sqlMigration(url, records));
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
