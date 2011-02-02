package com.googlecode.propidle.indexing;

import com.googlecode.propidle.PathType;
import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.search.FileNameSearcher;
import com.googlecode.propidle.search.LuceneFileNameSearcher;
import com.googlecode.propidle.search.Query;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.IOException;

import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.indexing.IndexWriterActivator.indexWriter;
import static com.googlecode.propidle.util.TemporaryIndex.emptyFileSystemDirectory;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasSize;
import static com.googlecode.totallylazy.matchers.NumberMatcher.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LuceneFileNameIndexerTest {
    private final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
    private final Directory directory = emptyFileSystemDirectory();// LucenePropertiesIndexerTest.directory("/Users/mattsavage/Desktop/lucene");
    private IndexWriter writer = indexWriter(directory, analyzer);

    private final FileNameIndexer propertiesIndexer = new LuceneFileNameIndexer(writer, Version.LUCENE_30);
    private final FileNameSearcher searcher = new LuceneFileNameSearcher(directory, analyzer, Version.LUCENE_30);

    @Test
    public void shouldFindFileNames() throws Exception {
        propertiesIndexer.index(PropertiesPath.propertiesPath("/production/roundhouseAWoodLouse"));
        propertiesIndexer.index(PropertiesPath.propertiesPath("/production/kneeAFlea"));
        propertiesIndexer.index(PropertiesPath.propertiesPath("/pilot/killAKrill"));
        writer.commit();

        assertThat(query("production"), hasSize(2));
        assertThat(query("pilot"), hasSize(1));
        assertThat(query("+kneeAFlea +production"), hasSize(1));
    }

    @Test
    public void shouldNotDuplicateFilenames() throws Exception {
        propertiesIndexer.index(PropertiesPath.propertiesPath("/production/shoeAShrew"));
        writer.commit();
        assertThat(query("shoeAShrew"), hasSize(1));

        propertiesIndexer.index(PropertiesPath.propertiesPath("/production/shoeAShrew"));
        writer.commit();
        assertThat(query("shoeAShrew"), hasSize(1));
    }

    @Test
    public void shouldAllowSearchingByParent() throws Exception {
        propertiesIndexer.index(propertiesPath("/production/shoeAShrew"));
        propertiesIndexer.index(propertiesPath("/production/shoeAShrew/123"));
        propertiesIndexer.index(propertiesPath("/production/shoeAShrew/125"));
        propertiesIndexer.index(propertiesPath("/pilot/shoeAShrew"));
        writer.commit();

        assertThat(findChildrenOf("/production/shoeAShrew"), hasSize(2));
        assertThat(findChildrenOf("production/shoeAShrew"), hasSize(2));
        assertThat(findChildrenOf("production/shoeAShrew/"), hasSize(2));

        assertThat(findChildrenOf("production"), hasSize(2)); // /production/shoeAShrew is both a DIRECTORY and a FILE

        assertThat(findChildrenOf("production/shoeAShrew").size(), equalTo(2));

        assertThat(findChildrenOf("/pilot"), hasSize(1));
        assertThat(findChildrenOf("pilot"), hasSize(1));
        assertThat(findChildrenOf("pilot/"), hasSize(1));

        assertThat(findChildrenOf("/pilot/shoeAShrew").size(), equalTo(0));
    }

    @Test
    public void shouldIndexFullHierarchy() throws Exception {
        propertiesIndexer.index(PropertiesPath.propertiesPath("/production/stabACrab/1234"));
        writer.commit();

        Sequence<Pair<PropertiesPath, PathType>> childrenOfRoot = findChildrenOf("/");
        assertThat(childrenOfRoot, hasSize(1));
        assertThat(childrenOfRoot.first(), is(pair(propertiesPath("/production"), PathType.DIRECTORY)));

        Sequence<Pair<PropertiesPath, PathType>> childrenOfProduction = findChildrenOf("/production");
        assertThat(childrenOfProduction, hasSize(1));
        assertThat(childrenOfProduction.first(), is(pair(propertiesPath("/production/stabACrab"), PathType.DIRECTORY)));

        Sequence<Pair<PropertiesPath, PathType>> childrenOfStabACrab = findChildrenOf("/production/stabACrab");
        assertThat(childrenOfStabACrab, hasSize(1));
        assertThat(childrenOfStabACrab.first(), is(pair(propertiesPath("/production/stabACrab/1234"), PathType.FILE)));

        assertThat(findChildrenOf("/production/stabACrab/1234").size(), equalTo(0));
    }

    private Sequence<Pair<PropertiesPath, PathType>> findChildrenOf(String path) {
        return sequence(searcher.childrenOf(propertiesPath(path)));
    }

    private Sequence<Pair<PropertiesPath, PathType>> query(String query) throws ParseException, IOException {
        Iterable<Pair<PropertiesPath, PathType>> results = searcher.search(Query.query(query));
        return sequence(results);
    }
}
