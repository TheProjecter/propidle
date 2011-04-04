package com.googlecode.propidle.server;

import com.googlecode.propidle.indexing.FileNameIndex;
import com.googlecode.propidle.indexing.PropertiesIndex;
import static com.googlecode.propidle.properties.Properties.properties;
import com.googlecode.propidle.properties.PropertiesPath;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.CHANGES;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.PROPERTIES_PATH;
import com.googlecode.propidle.versioncontrol.changes.Change;
import static com.googlecode.propidle.versioncontrol.changes.Change.applyChange;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import static com.googlecode.totallylazy.Pair.pair;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

public class RebuildIndex implements Callable<Void> {
    private final Records records;
    private final FileNameIndex fileNameIndexer;
    private final PropertiesIndex propertiesIndexer;

    public RebuildIndex(Records records, FileNameIndex fileNameIndexer, PropertiesIndex propertiesIndexer) {
        this.records = records;
        this.fileNameIndexer = fileNameIndexer;
        this.propertiesIndexer = propertiesIndexer;
    }

    public Void call() throws Exception {
        Sequence<Record> changesSortedByPropertiesFile = records.get(CHANGES).sortBy(PROPERTIES_PATH).realise();

        Sequence<Sequence<Record>> changesByProperties = changesByProperties(changesSortedByPropertiesFile);

        Sequence<Pair<PropertiesPath, Properties>> properties = changesByProperties.map(deserialise()).map(toProperties());

        properties.fold(fileNameIndexer, indexFileName());
        properties.fold(propertiesIndexer, indexProperties());
        return null;
    }

    private Sequence<Sequence<Record>> changesByProperties(Sequence<Record> allChanges) {
        List<List<Record>> allLists = new ArrayList<List<Record>>();
        List<Record> currentList = null;

        String currentPath = null;
        for (Record change : allChanges) {
            if (!change.get(PROPERTIES_PATH).equals(currentPath)) {
                if (currentList != null) allLists.add(currentList);
                currentList = new ArrayList<Record>();
                currentPath = change.get(PROPERTIES_PATH);
            }
            currentList.add(change);
        }
        if (currentList != null) allLists.add(currentList);
        return sequence(allLists).map(toSequence(Record.class));
    }

    private static <T> Callable1<? super List<T>, Sequence<T>> toSequence(Class<T> aClass) {
        return toSequence();
    }

    private static <T> Callable1<? super List<T>, Sequence<T>> toSequence() {
        return new Callable1<List<T>, Sequence<T>>() {
            public Sequence<T> call(List<T> list) throws Exception {
                return sequence(list);
            }
        };
    }

    private Callable1<? super Sequence<Record>, Sequence<Change>> deserialise() {
        return new Callable1<Sequence<Record>, Sequence<Change>>() {
            public Sequence<Change> call(Sequence<Record> recordSequence) throws Exception {
                return recordSequence.map(AllChangesFromRecords.deserialise());
            }
        };
    }

    private static Callable2<? super PropertiesIndex, ? super Pair<PropertiesPath, Properties>, PropertiesIndex> indexProperties() {
        return new Callable2<PropertiesIndex, Pair<PropertiesPath, Properties>, PropertiesIndex>() {
            public PropertiesIndex call(PropertiesIndex propertiesIndexer, Pair<PropertiesPath, Properties> pathAndProperties) throws Exception {
                propertiesIndexer.set(pathAndProperties);
                return propertiesIndexer;
            }
        };
    }

    private static Callable2<? super FileNameIndex, ? super Pair<PropertiesPath, Properties>, FileNameIndex> indexFileName() {
        return new Callable2<FileNameIndex, Pair<PropertiesPath, Properties>, FileNameIndex>() {
            public FileNameIndex call(FileNameIndex fileNameIndexer, Pair<PropertiesPath, Properties> pathAndProperties) throws Exception {
                fileNameIndexer.set(pathAndProperties.first());
                return fileNameIndexer;
            }
        };
    }

    private Callable1<? super Sequence<Change>, Pair<PropertiesPath, Properties>> toProperties() {
        return new Callable1<Sequence<Change>, Pair<PropertiesPath, Properties>>() {
            public Pair<PropertiesPath, Properties> call(Sequence<Change> changes) throws Exception {
                return pair(
                        propertiesPath(changes.first().propertiesPath().toString()),
                        changes.fold(properties(), applyChange()));
            }
        };
    }
}
