package com.googlecode.utterlyidle.migrations.sql;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.utterlyidle.migrations.Migration;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.SqlDialect;

import java.net.URL;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.migrations.Migration.migration;
import static com.googlecode.utterlyidle.migrations.sql.SqlMigration.parseMigrationFileId;
import static com.googlecode.utterlyidle.migrations.sql.SqlMigration.sqlMigration;
import static com.googlecode.utterlyidle.migrations.util.reflection.CodeSourceEntry.getUrl;
import static com.googlecode.utterlyidle.migrations.util.reflection.Packages.filesInOrUnderPackage;

public class SqlMigrations {


    public static Iterable<Migration> sqlMigrationsInSamePackageAs(Class aClass, SqlRecords records, SqlDialect sqlDialect) {
        return migrationFilesFromUrls(records, sequence(filesInOrUnderPackage(aClass)).map(getUrl()).filter(sqlFiles(sqlDialect)), sqlDialect);
    }

    private static Iterable<Migration> migrationFilesFromUrls(SqlRecords records, Iterable<URL> urls, SqlDialect sqlDialect) {
        return sequence(urls).map(migrationUsing(records, sqlDialect));
    }

    private static Callable1<? super URL, Migration> migrationUsing(final SqlRecords records, final SqlDialect sqlDialect) {
        return new Callable1<URL, Migration>() {
            public Migration call(URL url) throws Exception {
                return migration(parseMigrationFileId(url), sqlMigration(url, records, sqlDialect));
            }
        };
    }

    private static Predicate<? super URL> sqlFiles(final SqlDialect sqlDialect) {
        return new Predicate<URL>() {
            public boolean matches(URL url) {
              return url.getFile().endsWith(".sql") && sequence(url.getFile().split("/")).reverse().second().equals(sqlDialect.name());
            }
        };
    }
}
