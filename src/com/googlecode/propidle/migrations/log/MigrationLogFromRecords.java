package com.googlecode.propidle.migrations.log;

import static com.googlecode.propidle.migrations.MigrationName.migrationName;
import com.googlecode.propidle.migrations.MigrationNumber;
import static com.googlecode.propidle.migrations.MigrationNumber.migrationNumber;
import com.googlecode.propidle.Coercions;
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
import java.sql.Timestamp;

public class MigrationLogFromRecords implements MigrationLog {
    public static final Keyword MIGRATION_LOG = keyword("migration_log");
    public static final Keyword<Object> MIGRATION_DATE = keyword("migration_date", Object.class);
    public static final Keyword<Number> MIGRATION_NUMBER = keyword("migration_number", Number.class);
    public static final Keyword<String> MIGRATION_NAME = keyword("migration_name", String.class);

    private final Records records;

    public MigrationLogFromRecords(Records records) {
        this.records = records;
    }

    public Option<MigrationLogItem> get(MigrationNumber migrationNumber) {
        return records.get(MIGRATION_LOG).filter(where(MIGRATION_NUMBER, is((Number) migrationNumber.value()))).headOption().map(deserialise());
    }

    public Iterable<MigrationLogItem> add(Iterable<MigrationLogItem> events) {
        records.add(MIGRATION_LOG, sequence(events).map(serialise()));
        return sequence(events).realise();
    }

    private Callable1<? super MigrationLogItem, Record> serialise() {
        return new Callable1<MigrationLogItem, Record>() {
            public Record call(MigrationLogItem auditItem) throws Exception {
                return record().
                        set(MIGRATION_DATE, new Timestamp(auditItem.dateRun().getTime())).
                        set(MIGRATION_NUMBER, auditItem.number().value()).
                        set(MIGRATION_NAME, auditItem.name().value());
            }
        };
    }

    private Callable1<? super Record, MigrationLogItem> deserialise() {
        return new Callable1<Record, MigrationLogItem>() {
            public MigrationLogItem call(Record record) throws Exception {
                return new MigrationLogItem(
                        Coercions.coerce(record.get(MIGRATION_DATE), Date.class),
                        migrationNumber(record.get(MIGRATION_NUMBER)),
                        migrationName(record.get(MIGRATION_NAME)));
            }
        };
    }

    public static Records defineMigrationEvents(Records records) {
        records.define(MIGRATION_LOG, MIGRATION_DATE, MIGRATION_NUMBER, MIGRATION_NAME);
        return records;
    }
}