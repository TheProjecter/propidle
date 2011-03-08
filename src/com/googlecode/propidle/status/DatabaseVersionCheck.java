package com.googlecode.propidle.status;

import com.googlecode.propidle.migrations.Migration;
import com.googlecode.propidle.migrations.PropIdleMigrations;
import com.googlecode.totallylazy.records.sql.SqlRecords;

import static com.googlecode.propidle.migrations.Migration.getMigrationNumber;
import static com.googlecode.propidle.migrations.sql.SqlMigrations.sqlMigrationsInSamePackageAs;
import static com.googlecode.propidle.status.StatusCheckName.statusCheckName;
import static com.googlecode.propidle.status.StatusCheckResult.statusCheckResult;
import static com.googlecode.totallylazy.Sequences.sequence;

public class DatabaseVersionCheck implements StatusCheck {
    private final SqlRecords sqlRecords;

    public DatabaseVersionCheck(SqlRecords sqlRecords) {
        this.sqlRecords = sqlRecords;
    }

    public StatusCheckResult check() throws Exception {
        Migration migration = sequence(sqlMigrationsInSamePackageAs(PropIdleMigrations.class, sqlRecords)).sortBy(getMigrationNumber()).last();

        return statusCheckResult(statusCheckName(DatabaseVersionCheck.class.getSimpleName())).add("Required version", migration.number().value());
    }
}
