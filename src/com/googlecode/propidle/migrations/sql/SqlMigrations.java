package com.googlecode.propidle.migrations.sql;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.propidle.migrations.Migration;
import com.googlecode.propidle.migrations.bootstrap.Bootstrapper;
import com.googlecode.propidle.migrations.persistence.jdbc.SqlDialect;
import com.googlecode.propidle.migrations.util.reflection.CodeSourceEntry;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import java.net.URL;

import static com.googlecode.propidle.migrations.util.reflection.Packages.packageContains;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.propidle.migrations.util.reflection.Packages.filesInOrUnderPackage;

public class SqlMigrations {


    public static Iterable<Migration> sqlMigrationsInSamePackageAs(Class aClass, SqlRecords records, SqlDialect sqlDialect) {
        return migrationFilesFromUrls(records, filesInOrUnderPackageButNotBootstrap(aClass).map(CodeSourceEntry.getUrl()).filter(sqlFiles(sqlDialect)), sqlDialect);
    }

    private static Sequence<CodeSourceEntry> filesInOrUnderPackageButNotBootstrap(Class aClass) {
        return sequence(filesInOrUnderPackage(aClass)).filter(not(packageContains(Bootstrapper.class.getPackage())));
    }

    private static Iterable<Migration> migrationFilesFromUrls(SqlRecords records, Iterable<URL> urls, SqlDialect sqlDialect) {
        return sequence(urls).map(migrationUsing(records, sqlDialect));
    }

    private static Callable1<? super URL, Migration> migrationUsing(final SqlRecords records, final SqlDialect sqlDialect) {
        return new Callable1<URL, Migration>() {
            public Migration call(URL url) throws Exception {
                return Migration.migration(SqlMigration.parseMigrationFileId(url), SqlMigration.sqlMigration(url, records, sqlDialect));
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
