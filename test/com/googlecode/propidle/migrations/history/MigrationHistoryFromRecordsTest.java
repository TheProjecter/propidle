package com.googlecode.propidle.migrations.history;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.propidle.util.TestRecords.testRecordsWithAllMigrationsRun;
import static com.googlecode.propidle.util.TestRecords.emptyTestRecords;
import static com.googlecode.propidle.migrations.MigrationNumber.migrationNumber;
import static com.googlecode.propidle.migrations.MigrationName.migrationName;
import static com.googlecode.propidle.migrations.history.MigrationHistoryFromRecords.defineMigrationEvents;

import java.util.Date;

public class MigrationHistoryFromRecordsTest {
    private final MigrationHistoryFromRecords migrationHistory = new MigrationHistoryFromRecords(defineMigrationEvents(emptyTestRecords()));

    @Test
    public void addsAndRetrievesEvents() throws Exception {

        Sequence<MigrationEvent> migrations = sequence(
                new MigrationEvent(new Date(), migrationNumber(1), migrationName("create_toddlers_table")),
                new MigrationEvent(new Date(), migrationNumber(2), migrationName("index_dribble_column")));

        migrationHistory.add(migrations);


        assertThat(migrationHistory.get(migrations.first().number()), is(option(migrations.first())));
        assertThat(migrationHistory.get(migrations.second().number()), is(option(migrations.second())));
    }
}
