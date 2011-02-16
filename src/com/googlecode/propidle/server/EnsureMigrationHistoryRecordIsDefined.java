package com.googlecode.propidle.server;

import static com.googlecode.propidle.migrations.history.MigrationHistoryFromRecords.defineMigrationEvents;
import com.googlecode.propidle.migrations.history.MigrationEvent;
import com.googlecode.propidle.migrations.Migrator;
import com.googlecode.propidle.migrations.Migration;
import com.googlecode.totallylazy.records.Records;

import java.sql.SQLException;
import java.util.concurrent.Callable;

public class EnsureMigrationHistoryRecordIsDefined implements Migrator {
    private final Migrator decorated;
    private final Records records;

    public EnsureMigrationHistoryRecordIsDefined(Migrator decorated, Records records) {
        this.decorated = decorated;
        this.records = records;
    }

    public Iterable<MigrationEvent> migrate(Iterable<Migration> migrations) {
        try {
            defineMigrationEvents(records);
        } catch (Exception e) {
            if (com.googlecode.totallylazy.Exceptions.find(e, SQLException.class).isEmpty()) {
                throw new RuntimeException("Could not create migration history table", e);
            }
        }
        return decorated.migrate(migrations);
    }
}
