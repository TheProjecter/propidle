package com.googlecode.propidle.indexing;

import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.server.IndexRebuilder;
import com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords;
import com.googlecode.propidle.versioncontrol.changes.Change;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.utterlyidle.annotations.POST;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.*;
import static com.googlecode.propidle.versioncontrol.changes.Change.applyChange;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;

@Path(RebuildIndexResource.NAME)
@Produces(TEXT_HTML)
public class RebuildIndexResource {
    public static final String NAME = "rebuildIndex";
    private final IndexRebuilder rebuildIndex;
    private final Records records;

    public RebuildIndexResource(IndexRebuilder rebuildIndex, Records records) {
        this.rebuildIndex = rebuildIndex;
        this.records = records;
    }

    @POST
    public String rebuildIndex() {
        final StringWriter stringWriter = new StringWriter();
        Sequence<Record> changesSortedByPropertiesFile = records.get(CHANGES).sortBy(PROPERTIES_PATH).realise();
        Sequence<Sequence<Record>> changesByProperties = changesByProperties(changesSortedByPropertiesFile);

        Sequence<Pair<PropertiesPath, Properties>> properties = changesByProperties.map(deserialise()).map(toProperties());

        rebuildIndex.index(properties, new PrintWriter(stringWriter));
        return stringWriter.toString();
    }


    private Callable1<? super Sequence<Record>, Sequence<Change>> deserialise() {
        return new Callable1<Sequence<Record>, Sequence<Change>>() {
            public Sequence<Change> call(Sequence<Record> recordSequence) throws Exception {
                return recordSequence.map(AllChangesFromRecords.deserialise());
            }
        };
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

    private Callable1<? super Sequence<Change>, Pair<PropertiesPath, Properties>> toProperties() {
        return new Callable1<Sequence<Change>, Pair<PropertiesPath, Properties>>() {
            public Pair<PropertiesPath, Properties> call(Sequence<Change> changes) throws Exception {
                return pair(
                        propertiesPath(changes.first().propertiesPath().toString()),
                        changes.fold(properties(), applyChange()));
            }
        };
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

}