package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.propidle.properties.PropertiesPath;
import static com.googlecode.propidle.diff.PropertyComparison.changedProperty;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import com.googlecode.propidle.properties.PropertyValue;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.numbers.Numbers;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import com.googlecode.totallylazy.records.Keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;

public class AllChangesFromRecords implements AllChanges {
    public static final Keyword<String> CHANGES = Keyword.keyword("changes", String.class);
    public static final Keyword<String> PROPERTIES_PATH = Keyword.keyword("properties_path", String.class);
    public static final Keyword<Integer> REVISION_NUMBER = Keyword.keyword("revision_number", Integer.class);
    public static final Keyword<String> PROPERTY_NAME = Keyword.keyword("property_name", String.class);
    public static final Keyword<String> PREVIOUS_VALUE = Keyword.keyword("previous_value", String.class);
    public static final Keyword<String> UPDATED_VALUE = Keyword.keyword("updated_value", String.class);

    public final Records records;

    public AllChangesFromRecords(Records records) {
        this.records = records;
    }

    public Iterable<Change> get(PropertiesPath propertiesPath) {
        return records.
                get(CHANGES).
                filter(where(PROPERTIES_PATH, is(propertiesPath.toString()))).
                map(deserialise());
    }

    public Iterable<Change> get(PropertiesPath propertiesPath, RevisionNumber revisionNumber) {
        return records.
                get(CHANGES).
                filter(
                        where(PROPERTIES_PATH, is(propertiesPath.toString())).
                                and(where(REVISION_NUMBER, is(revisionNumber.value())))).
                map(deserialise());
    }

    public AllChangesFromRecords put(Iterable<Change> changes) {
        sequence(changes).map(serialise()).fold(records, addChange());
        return this;
    }

    private Callable2<? super Records, ? super Record, Records> addChange() {
        return new Callable2<Records, Record, Records>() {
            public Records call(Records records, Record record) throws Exception {
                records.add(CHANGES, record);
                return records;
            }
        };
    }

    private Callable1<? super Change, Record> serialise() {
        return new Callable1<Change, Record>() {
            public Record call(Change change) throws Exception {
                return record().
                        set(PROPERTIES_PATH, change.propertiesPath().toString()).
                        set(REVISION_NUMBER, change.revisionNumber().value()).
                        set(PROPERTY_NAME, change.propertyName().value()).
                        set(PREVIOUS_VALUE, change.previous().map(method(on(PropertyValue.class).value())).getOrNull()).
                        set(UPDATED_VALUE, change.updated().map(method(on(PropertyValue.class).value())).getOrNull());
            }
        };
    }

    private Callable1<? super Record, Change> deserialise() {
        return new Callable1<Record, Change>() {
            public Change call(Record record) throws Exception {
                return new Change(
                        revisionNumber(record.get(REVISION_NUMBER)),
                        PropertiesPath.propertiesPath(record.get(PROPERTIES_PATH)),
                        changedProperty(
                                propertyName(record.get(PROPERTY_NAME)),
                                propertyValue(record.get(PREVIOUS_VALUE)),
                                propertyValue(record.get(UPDATED_VALUE)))
                );
            }
        };
    }

    public static Records defineChangesRecord(Records records) {
        records.define(CHANGES, PROPERTIES_PATH, REVISION_NUMBER, PROPERTY_NAME, PREVIOUS_VALUE, UPDATED_VALUE);
        return records;
    }
}
