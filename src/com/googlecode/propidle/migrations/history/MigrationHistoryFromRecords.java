package com.googlecode.propidle.migrations.history;

import static com.googlecode.propidle.migrations.MigrationNumber.migrationNumber;
import static com.googlecode.propidle.migrations.MigrationName.migrationName;
import com.googlecode.propidle.migrations.MigrationNumber;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.totallylazy.records.Keyword;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;

import java.util.Date;

public class MigrationHistoryFromRecords implements MigrationHistory {
    public static final Keyword MIGRATION_EVENTS = keyword("migration_events");
    public static final Keyword<Date> MIGRATION_DATE = keyword("migration_date", Date.class);
    public static final Keyword<Integer> MIGRATION_NUMBER = keyword("migration_number", Integer.class);
    public static final Keyword<String> MIGRATION_NAME = keyword("migration_name", String.class);

    private final Records records;

    public MigrationHistoryFromRecords(Records records) {
        this.records = records;
    }

    public Option<MigrationEvent> get(MigrationNumber migrationNumber) {
        return records.get(MIGRATION_EVENTS).filter(where(MIGRATION_NUMBER, is(migrationNumber.value()))).headOption().map(deserialise());
    }

    public Iterable<MigrationEvent> add(Iterable<MigrationEvent> events) {
        records.add(MIGRATION_EVENTS, sequence(events).map(serialise()));
        return sequence(events).realise();
    }

    private Callable1<? super MigrationEvent, Record> serialise() {
        return new Callable1<MigrationEvent, Record>() {
            public Record call(MigrationEvent event) throws Exception {
                return record().
                        set(MIGRATION_DATE, event.dateRun()).
                        set(MIGRATION_NUMBER, event.number().value()).
                        set(MIGRATION_NAME, event.name().value());
            }
        };
    }

    private Callable1<? super Record, MigrationEvent> deserialise() {
        return new Callable1<Record, MigrationEvent>() {
            public MigrationEvent call(Record record) throws Exception {
                return new MigrationEvent(
                        record.get(MIGRATION_DATE),
                        migrationNumber(record.get(MIGRATION_NUMBER)),
                        migrationName(record.get(MIGRATION_NAME)));
            }
        };
    }

    public static Records defineMigrationEvents(Records records){
        records.define(MIGRATION_EVENTS, MIGRATION_DATE, MIGRATION_NUMBER, MIGRATION_NAME);
        return records;
    }
}
