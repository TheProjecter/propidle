package com.googlecode.propidle.indexing;

import com.googlecode.lazyrecords.Records;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.propidle.server.IndexRebuilder;
import com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords;
import com.googlecode.propidle.versioncontrol.changes.Change;
import com.googlecode.propidle.versioncontrol.changes.ChildPathsFromRecords;
import com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords;
import com.googlecode.propidle.versioncontrol.revisions.LuceneHighestRevisionIndex;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import static com.googlecode.lazyrecords.Record.constructors.record;
import static com.googlecode.propidle.indexing.IndexWriterActivator.indexWriter;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.properties.PropertyComparison.createdProperty;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.propidle.util.TemporaryIndex.emptyFileSystemDirectory;
import static com.googlecode.propidle.util.TestRecords.testRecordsWithAllMigrationsRun;
import static com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords.HIGHEST_REVISION;
import static com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords.REVISION_NUMBER;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

public class RebuildIndexResourceTest {

    private IndexRebuilder indexRebuilder = mock(IndexRebuilder.class);
    private final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
    private final Directory directory = emptyFileSystemDirectory();// LucenePropertiesIndexerTest.directory("/Users/mattsavage/Desktop/lucene");
    private IndexWriter writer = indexWriter(directory, analyzer);
    private Records records = testRecordsWithAllMigrationsRun();
    private AllChangesFromRecords changes = new AllChangesFromRecords(records, new ChildPathsFromRecords(records));
    private PropertiesPath propertiesPath = propertiesPath("/path");
    private PropertyName propertyName = propertyName("name");

    @Test
    public void shouldRebuildIndexWithTheRightValues() throws IOException {
        changes.put(sequence(new Change(revisionNumber("1"), propertiesPath, createdProperty(propertyName, propertyValue("value1")))));
        changes.put(sequence(new Change(revisionNumber("3"), propertiesPath, createdProperty(propertyName, propertyValue("value3")))));
        changes.put(sequence(new Change(revisionNumber("2"), propertiesPath, createdProperty(propertyName, propertyValue("value2")))));

        rebuildIndex();

        Properties expectedProperties = new Properties();
        expectedProperties.put(propertyName.value(), "value3");
        Sequence<Pair<PropertiesPath, Properties>> expectedValue= sequence(pair(propertiesPath, expectedProperties));
        verify(indexRebuilder).index(argThat(hasExactly(expectedValue)), any(PrintWriter.class));
    }

    @Test
    public void shouldNotRebuildIndexIfNoNewChanges() throws Exception {
        givenHighestRevisionNumberIs(10);
        rebuildIndex();
        thenIndexRebuildingWasTriggered();

        rebuildIndex();
        thenIndexRebuildingWasNotTriggered();
    }

    @Test
    public void shouldRebuildIndexIfNewChange() throws Exception {
        givenHighestRevisionNumberIs(10);
        rebuildIndex();
        thenIndexRebuildingWasTriggered();

        givenHighestRevisionNumberIs(11);
        rebuildIndex();
        thenIndexRebuildingWasTriggered();
    }

    @Test
    public void shouldRebuildIndexIfNotSuccessfulThePreviousTime() throws Exception {
        givenHighestRevisionNumberIs(10);
        rebuildIndex();
        thenIndexRebuildingWasTriggered();

        givenHighestRevisionNumberIs(11);
        givenIndexRebuilderThrowsAnException();
        try {
            rebuildIndex();
            fail();
        } catch (Exception expected){}

        thenIndexRebuildingWasTriggered();

        rebuildIndex();
        thenIndexRebuildingWasTriggered();
    }

    private void rebuildIndex() throws IOException {
        try {
            final HighestRevisionNumbersFromRecords highestRevisionNumbers = new HighestRevisionNumbersFromRecords(records);
            final LuceneHighestRevisionIndex highestRevisionIndex = new LuceneHighestRevisionIndex(writer, directory, analyzer, Version.LUCENE_30);
            final RebuildIndexResource rebuildIndexResource = new RebuildIndexResource(indexRebuilder, records, highestRevisionNumbers, highestRevisionIndex);
            rebuildIndexResource.rebuildIndex();
        } finally {
            writer.commit();
        }
    }

    private void givenIndexRebuilderThrowsAnException() {
        doThrow(new RuntimeException()).when(indexRebuilder).index(any(Iterable.class), any(PrintWriter.class));
    }

    private void givenHighestRevisionNumberIs(int revisionNumber) {
        records.remove(HIGHEST_REVISION);
        records.add(HIGHEST_REVISION, record().set(REVISION_NUMBER, revisionNumber));
    }

    private void thenIndexRebuildingWasTriggered() {
        verify(indexRebuilder).index(any(Iterable.class), any(PrintWriter.class));
        reset(indexRebuilder);
    }


    private void thenIndexRebuildingWasNotTriggered() {
        verifyZeroInteractions(indexRebuilder);
    }
}
