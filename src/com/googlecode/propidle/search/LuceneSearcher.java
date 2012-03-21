package com.googlecode.propidle.search;

import com.googlecode.totallylazy.Callable1;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PositiveScoresOnlyCollector;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import static com.googlecode.totallylazy.Sequences.sequence;

public class LuceneSearcher {
    private final Analyzer analyzer;
    private final Version version;

    public LuceneSearcher(Analyzer analyzer, Version version) {
        this.analyzer = analyzer;
        this.version = version;
    }

    public Iterable<Document> search(Directory directory, Query query) {
        if (query.isEmpty()) return sequence();
        try {
            IndexSearcher searcher = new IndexSearcher(directory, true);
            TopScoreDocCollector topScores = TopScoreDocCollector.create(100, true);
            PositiveScoresOnlyCollector collector = new PositiveScoresOnlyCollector(topScores);

            searcher.search(toQuery(query), collector);
            return sequence(topScores.topDocs().scoreDocs).map(toDocument(searcher));
        } catch (Exception e) {
            throw new SearchException("Problem with query " + query, e);
        }
    }

    private org.apache.lucene.search.Query toQuery(Query query) throws ParseException {
        MultiFieldQueryParser parser = new MultiFieldQueryParser(version,sequence(query.fieldNames()).toArray(String.class),analyzer);
        parser.setAllowLeadingWildcard(true);
        return parser.parse(query.query());
    }

    private Callable1<? super ScoreDoc, Document> toDocument(final IndexSearcher searcher) {
        return new Callable1<ScoreDoc, Document>() {
            public Document call(ScoreDoc scoreDoc) throws Exception {
                Document document = searcher.doc(scoreDoc.doc);
                return document;
            }
        };
    }

}
