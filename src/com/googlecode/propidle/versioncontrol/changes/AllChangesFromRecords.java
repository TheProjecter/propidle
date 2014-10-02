package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.lazyrecords.Definition;
import com.googlecode.lazyrecords.Keyword;
import com.googlecode.lazyrecords.Record;
import com.googlecode.lazyrecords.Records;
import com.googlecode.propidle.PathType;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.properties.PropertyValue;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.utterlyidle.io.HierarchicalPath;

import static com.googlecode.lazyrecords.Definition.constructors.definition;
import static com.googlecode.lazyrecords.Keyword.constructors.keyword;
import static com.googlecode.lazyrecords.Record.constructors.record;
import static com.googlecode.propidle.PathType.DIRECTORY;
import static com.googlecode.propidle.PathType.FILE;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.properties.PropertyComparison.changedProperty;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.empty;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;

public class AllChangesFromRecords implements AllChanges {
    public static final Keyword<String> PROPERTIES_PATH = keyword("properties_path", String.class);
    public static final Keyword<Integer> REVISION_NUMBER = keyword("revision_number", Integer.class);
    public static final Keyword<String> PROPERTY_NAME = keyword("property_name", String.class);
    public static final Keyword<String> PREVIOUS_VALUE = keyword("previous_value", String.class);
    public static final Keyword<String> UPDATED_VALUE = keyword("updated_value", String.class);
    public static final Definition CHANGES = definition("changes", PROPERTIES_PATH, REVISION_NUMBER, PROPERTY_NAME, PREVIOUS_VALUE, UPDATED_VALUE);
    public static final Definition LATEST_CHANGES_VIEW = definition("latest_changes_view", PROPERTIES_PATH, REVISION_NUMBER, PROPERTY_NAME, PREVIOUS_VALUE, UPDATED_VALUE);

    private final Records records;
    private final ChildPaths childPaths;

    public AllChangesFromRecords(Records records, ChildPaths childPaths) {
        this.records = records;
        this.childPaths = childPaths;
    }

    public Iterable<Change> get(PropertiesPath propertiesPath) {
        return records.
                get(CHANGES).
                filter(where(PROPERTIES_PATH, is(propertiesPath.toString()))).
                map(deserialise());
    }

    @Override
    public Iterable<Change> getLatestChanges(PropertiesPath propertiesPath) {
        return records.
                get(LATEST_CHANGES_VIEW).
                filter(where(PROPERTIES_PATH, is(propertiesPath.toString()))).
                sortBy(PROPERTY_NAME).
                map(deserialise());
    }

    public Iterable<Pair<PropertiesPath, PathType>> childrenOf(PropertiesPath parent) {
        return childPaths.childPaths(parent).map(toPairs(parent)).unique();
    }

    private Function1<PropertiesPath, Pair<PropertiesPath, PathType>> toPairs(final PropertiesPath parent) {
        return new Function1<PropertiesPath, Pair<PropertiesPath, PathType>>() {
            public Pair<PropertiesPath, PathType> call(PropertiesPath propertiesPath) throws Exception {
                return pair(childPath(propertiesPath, parent), type(propertiesPath, parent));
            }
        };
    }

    private PropertiesPath childPath(HierarchicalPath propertiesPath, HierarchicalPath parent) {
        return propertiesPath(parent.subDirectory(findCurrentNodeName(propertiesPath, parent)));
    }

    private String findCurrentNodeName(HierarchicalPath propertiesPath, HierarchicalPath parent) {
        return propertiesPath.remove(parent).segments().filter(not(empty())).headOption().getOrElse("");
    }

    private PathType type(PropertiesPath propertiesPath, PropertiesPath parent) {
        HierarchicalPath path = propertiesPath.remove(parent);
        return path.segments().filter(not(empty())).size() > 1 ? DIRECTORY : FILE;
    }



    public Iterable<Change> get(PropertiesPath propertiesPath, RevisionNumber revisionNumber) {
        return records.
                get(CHANGES).
                filter(
                        where(PROPERTIES_PATH, is(propertiesPath.toString())).
                                and(where(REVISION_NUMBER, is((Number) revisionNumber.value())))).
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

    public static Callable1<? super Record, Change> deserialise() {
        return new Callable1<Record, Change>() {
            public Change call(Record record) throws Exception {
                return new Change(
                        revisionNumber(record.get(REVISION_NUMBER)),
                        propertiesPath(record.get(PROPERTIES_PATH)),
                        changedProperty(
                                propertyName(record.get(PROPERTY_NAME)),
                                propertyValue(record.get(PREVIOUS_VALUE)),
                                propertyValue(record.get(UPDATED_VALUE)))
                );
            }
        };
    }
}
