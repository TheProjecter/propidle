package com.googlecode.propidle.search;

import com.googlecode.propidle.PathType;
import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.indexing.LuceneFileNameIndexer;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.indexing.LuceneFileNameIndexer.*;
import static com.googlecode.propidle.search.Query.query;
import static com.googlecode.totallylazy.Pair.pair;

public class LuceneFileNameSearcher implements FileNameSearcher {
    private final Directory directory;
    private final LuceneSearcher allFieldsSearcher;
    private final LuceneSearcher parentSearcher;

    public LuceneFileNameSearcher(Directory directory, Analyzer analyzer, Version version) {
        this.allFieldsSearcher = new LuceneSearcher(analyzer, version);
        this.parentSearcher = new LuceneSearcher(new KeywordAnalyzer(), version);
        this.directory = directory;
    }

    public Iterable<Pair<PropertiesPath, PathType>> search(Query query) {
        Query modifiedQuery = query.
                againstFields(ALL_FIELDS).
                and(String.format("+%s:\"%s\"", PATH_TYPE, PathType.FILE));
        return allFieldsSearcher.search(directory, modifiedQuery, deserialize());
    }

    public Iterable<Pair<PropertiesPath, PathType>> childrenOf(PropertiesPath path) {
        Query query = query(String.format("+%s:\"%s\"", LuceneFileNameIndexer.PARENT, path));
        return parentSearcher.search(directory, query, deserialize());
    }

    private Callable1<? super Document, Pair<PropertiesPath, PathType>> deserialize() {
        return new Callable1<Document, Pair<PropertiesPath, PathType>>() {
            public Pair<PropertiesPath, PathType> call(Document document) throws Exception {
                return pair(propertiesPath(document.get(PATH)), PathType.tryToResolve(document.get(PATH_TYPE)));
            }
        };
    }
}
