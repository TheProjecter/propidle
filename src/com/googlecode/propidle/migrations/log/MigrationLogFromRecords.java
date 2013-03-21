package com.googlecode.propidle.migrations.log;

import com.googlecode.lazyrecords.Definition;
import com.googlecode.lazyrecords.Keyword;
import com.googlecode.lazyrecords.Record;
import com.googlecode.lazyrecords.Records;
import com.googlecode.propidle.migrations.MigrationNumber;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.propidle.migrations.MigrationNumber;
import com.googlecode.propidle.migrations.ModuleName;

import java.util.Date;

import static com.googlecode.lazyrecords.Keywords.keyword;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.propidle.migrations.MigrationName.migrationName;
import static com.googlecode.propidle.migrations.MigrationNumber.migrationNumber;
import static com.googlecode.propidle.migrations.ModuleName.moduleName;

public class MigrationLogFromRecords implements MigrationLog {
    public static final Keyword<Date> MIGRATION_DATE = keyword("migration_date", Date.class);
    public static final Keyword<Integer> MIGRATION_NUMBER = keyword("migration_number", Integer.class);
    public static final Keyword<String> MIGRATION_NAME = keyword("migration_name", String.class);
    public static final Keyword<String> MODULE_NAME = keyword("module_name", String.class);
    public static final Definition MIGRATION_LOG = Definition.constructors.definition("migration_log", MIGRATION_DATE, MIGRATION_NUMBER, MIGRATION_NAME, MODULE_NAME);

    private final Records records;

    public MigrationLogFromRecords(Records records) {
        this.records = records;
    }

    public Option<MigrationLogItem> get(MigrationNumber migrationNumber, ModuleName moduleName) {
        LogicalPredicate<Record> logicalPredicate = where(MIGRATION_NUMBER, is((Number) migrationNumber.value()));
        LogicalPredicate<Record> logicalPredicate2 = where(MODULE_NAME, is(moduleName.value()));
        LogicalPredicate<Record> where = logicalPredicate.and(logicalPredicate2);
        return records.get(MIGRATION_LOG).filter(where).headOption().map(deserialise());
    }

    public Iterable<MigrationLogItem> add(Iterable<MigrationLogItem> events) {
        records.add(MIGRATION_LOG, sequence(events).map(serialise()));
        return sequence(events).realise();
    }

    public Iterable<MigrationLogItem> list() {
        return records.get(MIGRATION_LOG).map(deserialise());
    }

    private Callable1<? super MigrationLogItem, Record> serialise() {
        return new Callable1<MigrationLogItem, Record>() {
            public Record call(MigrationLogItem auditItem) throws Exception {
                return Record.constructors.record().
                        set(MIGRATION_DATE, auditItem.dateRun()).
                        set(MIGRATION_NUMBER, auditItem.number().value()).
                        set(MODULE_NAME, auditItem.moduleName().value()).
                        set(MIGRATION_NAME, auditItem.name().value());
            }
        };
    }

    private Callable1<? super Record, MigrationLogItem> deserialise() {
        return new Callable1<Record, MigrationLogItem>() {
            public MigrationLogItem call(Record record) throws Exception {
                return new MigrationLogItem(
                        record.get(MIGRATION_DATE),
                        MigrationNumber.migrationNumber(record.get(MIGRATION_NUMBER)),
                        migrationName(record.get(MIGRATION_NAME)), moduleName(record.get(MODULE_NAME))
                );
            }
        };
    }
}
