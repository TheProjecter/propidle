package com.googlecode.propidle.migrations.sql;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.propidle.migrations.MigrationName;
import com.googlecode.propidle.migrations.MigrationNumber;
import com.googlecode.propidle.migrations.util.Urls;
import com.googlecode.propidle.migrations.MigrationId;
import com.googlecode.propidle.migrations.persistence.jdbc.SqlDialect;

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.googlecode.lazyrecords.sql.expressions.Expressions.textOnly;
import static com.googlecode.propidle.migrations.MigrationId.migrationId;
import static com.googlecode.propidle.migrations.MigrationName.migrationName;
import static com.googlecode.propidle.migrations.MigrationNumber.migrationNumber;
import static com.googlecode.propidle.migrations.util.Urls.getUrl;
import static java.lang.String.format;

public class SqlMigration implements Runnable {
    private static final Pattern pattern = Pattern.compile(".*/([0-9]+)\\-(.*)\\.sql$");
    private final SqlRecords records;
    private final Callable<SqlFile> sql;
    private final SqlDialect sqlDialect;

    public static SqlMigration sqlMigration(URL url, SqlRecords records, final SqlDialect sqlDialect) {
        return sqlMigration(asSqlFile(url), records, sqlDialect);
    }

    private static Callable<SqlFile> asSqlFile(final URL url) {
        return new Callable<SqlFile>() {
            public SqlFile call() throws Exception {
                return SqlFile.sqlFile(Urls.getUrl(url).call());
            }
        };
    }

    private static SqlMigration sqlMigration(Callable<SqlFile> sql, SqlRecords records, final SqlDialect sqlDialect) {
        return new SqlMigration(records, sql, sqlDialect);
    }

    protected SqlMigration(SqlRecords records, Callable<SqlFile> sql, SqlDialect sqlDialect) {
        this.records = records;
        this.sql = sql;
        this.sqlDialect = sqlDialect;
    }

    public void run() {
        Iterable<SqlLine> lines = sqlDialect.linesIn(sqlFile());
        for (SqlLine line : lines) {
            try {
                records.update(textOnly(line.toString()));
            } catch (Exception e) {
                throw new RuntimeException("Could not execute sql migration:\n" + line.toString(), e);
            }
        }
    }

    private SqlFile sqlFile() {
        try {
            return sql.call();
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve sql for migration", e);
        }
    }

    public static MigrationId parseMigrationFileId(URL url) {
        Matcher matcher = pattern.matcher(url.getFile());
        if (!matcher.find()) {
            throw new IllegalArgumentException(
                    format("URL should end in '/[number]-[name].sql', eg '/001-create_users.sql'. '%s' is not valid", url));
        }
        return migrationId(
                MigrationNumber.migrationNumber(java.lang.Integer.parseInt(matcher.group(1))),
                MigrationName.migrationName(matcher.group(2)));
    }
}
