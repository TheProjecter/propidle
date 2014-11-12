package com.googlecode.propidle.versioncontrol.revisions;

import com.googlecode.propidle.search.LuceneSearcher;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Option;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import static com.googlecode.propidle.search.Query.query;
import static com.googlecode.propidle.versioncontrol.revisions.HighestExistingRevisionNumber.highestExistingRevisionNumber;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.String.format;


public class LuceneHighestRevisionIndex implements HighestRevisionIndex {
    public static final String HIGHEST_REVISION = "highest.revision";
    public static final String HIGHEST_REVISION_QUERY = format("%s:*", HIGHEST_REVISION);
    private final IndexWriter writer;
    private final QueryParser queryParser;
    private final Directory directory;
    private final LuceneSearcher searcher;

    public LuceneHighestRevisionIndex(IndexWriter writer, Directory directory, Analyzer analyzer, Version version) {
        this.writer = writer;
        this.directory = directory;
        this.queryParser = new QueryParser(version, HIGHEST_REVISION, new KeywordAnalyzer());
        queryParser.setAllowLeadingWildcard(true);
        this.searcher = new LuceneSearcher(analyzer, version);
    }

    @Override
    public void set(HighestExistingRevisionNumber revisionNumber) {
        try {
            writer.deleteDocuments(highestRevisionNotNull());
            writer.addDocument(highestRevisionDoc(revisionNumber));
        } catch (Throwable e) {
            throw LazyException.lazyException(e);
        }
    }

    @Override
    public Option<HighestExistingRevisionNumber> get() {
        return sequence(searcher.search(directory, query(HIGHEST_REVISION_QUERY))).map(highestRevision()).headOption();
    }

    private Query highestRevisionNotNull() throws ParseException {
        return queryParser.parse(HIGHEST_REVISION_QUERY);
    }


    private Document highestRevisionDoc(HighestExistingRevisionNumber revisionNumber) {
        final Document document = new Document();
        document.add(new Field(HIGHEST_REVISION, revisionNumber.value().toString(), Field.Store.YES, Field.Index.ANALYZED));
        return document;
    }

    private Callable1<Document, HighestExistingRevisionNumber> highestRevision() {
        return new Callable1<Document, HighestExistingRevisionNumber>() {
            @Override
            public HighestExistingRevisionNumber call(Document document) throws Exception {
                return highestExistingRevisionNumber(Integer.valueOf(document.get(HIGHEST_REVISION)));
            }
        };
    }

}
