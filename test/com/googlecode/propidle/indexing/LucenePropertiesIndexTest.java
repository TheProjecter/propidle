package com.googlecode.propidle.indexing;

import static com.googlecode.propidle.indexing.IndexWriterActivator.indexWriter;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import com.googlecode.propidle.search.LucenePropertiesSearcher;
import com.googlecode.propidle.search.Query;
import com.googlecode.propidle.search.SearchResult;
import static com.googlecode.totallylazy.Pair.pair;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.NumberMatcher.equalTo;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

import java.io.IOException;

public class LucenePropertiesIndexTest {
    private final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
    private final Directory directory = new RAMDirectory();

    private IndexWriter writer = indexWriter(directory, analyzer);
    private final LucenePropertiesIndex propertiesIndexer = new LucenePropertiesIndex(writer, Version.LUCENE_30);

    private final LucenePropertiesSearcher searcher = new LucenePropertiesSearcher(directory, analyzer, Version.LUCENE_30);

    @Test
    public void shouldMakePropertiesDiscoverable() throws Exception {
        propertiesIndexer.set(pair(
                propertiesPath("/properties/production/attackAYak"),
                properties(
                        pair("number.of.threads", "12"),
                        pair("amount.of.chutzpah", "Elizabeth Taylor"))));

        propertiesIndexer.set(pair(
                propertiesPath("/properties/production/eviscerateASkate"),
                properties(
                        pair("number.of.threads", "80 million bajillion"),
                        pair("special.sauce", "seeping"))));
        writer.commit();

        assertThat(query("bajillion").size(), equalTo(1));
        assertThat(query("12").size(), equalTo(1));
        assertThat(query("threads").size(), equalTo(2));
        assertThat(query("number of threads").size(), equalTo(2));
        assertThat(query("number.of.*").size(), equalTo(2));
    }

    @Test
    public void shouldDeleteOldDocumentForPropertiesFile() throws Exception {
        propertiesIndexer.set(pair(
                propertiesPath("/properties/production/shoeAShrew"),
                properties(pair("to.be.deleted", "it's gawn"))));

        propertiesIndexer.set(pair(
                propertiesPath("/properties/production/shoeAShrew"),
                properties()));

        writer.commit();

        assertThat(query("to.be.deleted").size(), equalTo(0));
    }

    private Sequence<SearchResult> query(String query) throws ParseException, IOException {
        Iterable<SearchResult> results = searcher.search(Query.query(query));
        return sequence(results);
    }
}
