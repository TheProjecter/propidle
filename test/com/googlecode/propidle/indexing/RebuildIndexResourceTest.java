package com.googlecode.propidle.indexing;

import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.propidle.server.IndexRebuilder;
import com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords;
import com.googlecode.propidle.versioncontrol.changes.Change;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.lazyrecords.memory.MemoryRecords;
import org.junit.Test;

import java.io.PrintWriter;
import java.util.Properties;

import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.properties.PropertyComparison.createdProperty;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RebuildIndexResourceTest {

    private IndexRebuilder indexRebuilder = mock(IndexRebuilder.class);
    private MemoryRecords records = new MemoryRecords();
    private AllChangesFromRecords changes = new AllChangesFromRecords(records);
    private PropertiesPath propertiesPath = propertiesPath("/path");
    private PropertyName propertyName = propertyName("name");
    private RebuildIndexResource rebuildIndexResource = new RebuildIndexResource(indexRebuilder, records);

    @Test
    public void shouldRebuildIndexWithTheRightValues() {
        changes.put(sequence(new Change(revisionNumber("1"), propertiesPath, createdProperty(propertyName, propertyValue("value1")))));
        changes.put(sequence(new Change(revisionNumber("3"), propertiesPath, createdProperty(propertyName, propertyValue("value3")))));
        changes.put(sequence(new Change(revisionNumber("2"), propertiesPath, createdProperty(propertyName, propertyValue("value2")))));

        rebuildIndexResource.rebuildIndex();

        Properties expectedProperties = new Properties();
        expectedProperties.put(propertyName.value(), "value3");
        Sequence<Pair<PropertiesPath, Properties>> expectedValue= sequence(pair(propertiesPath, expectedProperties));
        verify(indexRebuilder).index((Iterable<Pair<PropertiesPath, Properties>>) argThat(hasExactly(expectedValue)), any(PrintWriter.class));
    }


}
