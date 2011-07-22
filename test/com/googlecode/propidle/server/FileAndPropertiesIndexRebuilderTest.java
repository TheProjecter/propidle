package com.googlecode.propidle.server;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertyComparison.createdProperty;

import com.googlecode.propidle.filenames.LuceneFileNameIndex;
import com.googlecode.propidle.properties.Properties;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.properties.PropertyComparison;
import com.googlecode.propidle.search.LucenePropertiesIndex;

import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;

import com.googlecode.propidle.search.FileNameSearcher;
import com.googlecode.propidle.search.LuceneFileNameSearcher;
import com.googlecode.propidle.search.LucenePropertiesSearcher;
import com.googlecode.propidle.search.PropertiesSearcher;

import static com.googlecode.propidle.search.Query.query;
import static com.googlecode.propidle.search.SearchResult.searchResult;

import com.googlecode.propidle.util.TestRecords;
import com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords;

import static com.googlecode.propidle.versioncontrol.changes.Change.change;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;

import com.googlecode.propidle.PathType;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Records;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class FileAndPropertiesIndexRebuilderTest {
    private static final Version VERSION = Version.LUCENE_30;

    private final Records records = TestRecords.testRecordsWithAllMigrationsRun();
    private final AllChangesFromRecords changes = new AllChangesFromRecords(records);
    private IndexWriter writer;
    private FileNameSearcher fileNameSearcher;
    private PropertiesSearcher propertiesSearcher;

    @Before
    public void createIndex() throws IOException {
        RAMDirectory directory = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer(VERSION);
        writer = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
        fileNameSearcher = new LuceneFileNameSearcher(directory, analyzer, VERSION);
        propertiesSearcher = new LucenePropertiesSearcher(directory, analyzer, VERSION);
    }

    @Test
    public void willRebuildIndicesFromChanges() throws Exception {
        FileAndPropertiesIndexRebuilder rebuildIndex = new FileAndPropertiesIndexRebuilder(
                new LuceneFileNameIndex(writer, VERSION),
                new LucenePropertiesIndex(writer, VERSION));

        Sequence<Pair<PropertiesPath, java.util.Properties>> propertiesToIndex = sequence(
                pair(propertiesPath("moo"), properties(pair("space.biscuit", "chewing on face palm"), Pair.pair("space.biscuit","shell fish souffle"))),
                pair(propertiesPath("not/moo"), properties(pair("moose.gizzards", "massive gongs"), Pair.pair("king.of.muscle","brian ferry"))),
                pair(propertiesPath("oook"), properties("king.of.muscle=brian ferry")));

        rebuildIndex.index(propertiesToIndex, new PrintWriter(new StringWriter()));
        writer.commit();

        assertThat(
                sequence(propertiesSearcher.search(query("massive"))).first(),
                is(searchResult(propertiesPath("not/moo"), propertyName("moose.gizzards"), propertyValue("massive gongs"))));

        assertThat(
                sequence(fileNameSearcher.search(query("oook"))).first(),
                is(pair(propertiesPath("oook"), PathType.FILE)));
    }
}
