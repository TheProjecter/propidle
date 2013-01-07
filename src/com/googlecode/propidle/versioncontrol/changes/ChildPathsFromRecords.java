package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.lazyrecords.Record;
import com.googlecode.lazyrecords.Records;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.lazyrecords.Grammar.first;
import static com.googlecode.lazyrecords.Grammar.is;
import static com.googlecode.lazyrecords.Grammar.maximum;
import static com.googlecode.lazyrecords.Grammar.not;
import static com.googlecode.lazyrecords.Grammar.notNullValue;
import static com.googlecode.lazyrecords.Grammar.select;
import static com.googlecode.lazyrecords.Grammar.to;
import static com.googlecode.lazyrecords.Grammar.where;
import static com.googlecode.propidle.properties.PropertiesPath.toPropertiesPath;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.CHANGES;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.PROPERTIES_PATH;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.PROPERTY_NAME;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.REVISION_NUMBER;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.UPDATED_VALUE;
import static com.googlecode.totallylazy.Sequences.reduce;
import static com.googlecode.totallylazy.Strings.startsWith;

public class ChildPathsFromRecords implements ChildPaths {
    private final Records records;

    public ChildPathsFromRecords(Records records) {
        this.records = records;
    }

    @Override
    public Sequence<PropertiesPath> childPaths(PropertiesPath parent) {
        Sequence<Record> maxRevisions = records.get(CHANGES).
                filter(where(PROPERTIES_PATH, startsWith(parent.toString())).
                        and(where(PROPERTIES_PATH, not(parent.toString())))).
                groupBy(select(PROPERTIES_PATH, PROPERTY_NAME)).
                map(reduce(to(first(PROPERTIES_PATH), first(PROPERTY_NAME), maximum(REVISION_NUMBER))));

        return maxRevisions.flatMap(new Mapper<Record, Option<String>>() {
            @Override
            public Option<String> call(Record record) throws Exception {
                return records.get(CHANGES).find(where(REVISION_NUMBER, is(record.get(maximum(REVISION_NUMBER)))).
                        and(where(PROPERTIES_PATH, is(record.get(first(PROPERTIES_PATH))))).
                        and(where(PROPERTY_NAME, is(record.get(first(PROPERTY_NAME))))).
                        and(where(UPDATED_VALUE, is(notNullValue())))).map(PROPERTIES_PATH);
            }
        }).map(toPropertiesPath());
    }

}
