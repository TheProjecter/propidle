package com.googlecode.propidle.migrations.sql;

import com.googlecode.propidle.Urls;
import com.googlecode.propidle.migrations.MigrationId;
import static com.googlecode.propidle.migrations.MigrationId.migrationId;
import static com.googlecode.propidle.migrations.MigrationName.migrationName;
import static com.googlecode.propidle.migrations.MigrationNumber.migrationNumber;

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.String.format;

public class SqlMigration implements Runnable {
    private static final Pattern pattern = Pattern.compile(".*/([0-9]+)\\-(.*)\\.sql$");
    private final SqlExecutor executor;
    private final Callable<String> sql;

    public static SqlMigration sqlMigration(URL url, SqlExecutor executor) {
        return sqlMigration(Urls.getUrl(url), executor);
    }

    public static SqlMigration sqlMigration(Callable<String> sql, SqlExecutor executor) {
        return new SqlMigration(executor, sql);
    }

    protected SqlMigration(SqlExecutor executor, Callable<String> sql) {
        this.executor = executor;
        this.sql = sql;
    }

    public void run() {
        String sqlString = sqlString();
        try {
            executor.execute(sqlString);
        } catch (Exception e) {
            throw new RuntimeException("Could not execute sql migration:\n" + sqlString, e);
        }
    }

    private String sqlString() {
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
                migrationNumber(java.lang.Integer.parseInt(matcher.group(1))),
                migrationName(matcher.group(2)));
    }
}
