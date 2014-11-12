package com.googlecode.propidle.versioncontrol.revisions;


import com.googlecode.totallylazy.Option;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.googlecode.propidle.indexing.IndexWriterActivator.indexWriter;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LuceneHighestRevisionIndexTest {
    private final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
    private final Directory directory = new RAMDirectory();
    private final IndexWriter writer = indexWriter(directory, analyzer);
    private final LuceneHighestRevisionIndex indexer = new LuceneHighestRevisionIndex(writer, directory, analyzer, Version.LUCENE_30);
    private QueryParser queryParser;

    @Before
    public void setupQueryParser(){
        queryParser = new QueryParser(Version.LUCENE_30, LuceneHighestRevisionIndex.HIGHEST_REVISION, new StandardAnalyzer(Version.LUCENE_30));
        queryParser.setAllowLeadingWildcard(true);
    }

    @Test
    public void shouldReturnNoneIfNoHighestRevisionSet() throws Exception {
        assertThat(totalNumOfDocuments(), is(0));
        assertThat(indexer.get(), is(Option.<HighestExistingRevisionNumber>none()));
    }

    @Test
    public void shouldSetHighestRevision() throws Exception {
        indexer.set(HighestExistingRevisionNumber.highestExistingRevisionNumber(5));
        writer.commit();

        assertThat(totalNumOfDocuments(), is(1));
        assertThat(indexer.get().get().value(), is(5));
    }

    @Test
    public void shouldDeletePreviousHighestRevision() throws Exception {
        indexer.set(HighestExistingRevisionNumber.highestExistingRevisionNumber(5));
        writer.commit();

        assertThat(totalNumOfDocuments(), is(1));

        indexer.set(HighestExistingRevisionNumber.highestExistingRevisionNumber(6));
        writer.commit();

        assertThat(totalNumOfDocuments(), is(1));
        assertThat(indexer.get().get().value(), is(6));
    }

    private int totalNumOfDocuments() throws IOException, ParseException {
        IndexSearcher indexSearcher = new IndexSearcher(directory, true);
        final Query query = queryParser.parse("*");

        final TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
        return topDocs.totalHits;
    }
}