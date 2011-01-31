package com.googlecode.propidle.search;

import com.googlecode.totallylazy.Callable1;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import static com.googlecode.propidle.PropertyName.propertyName;
import static com.googlecode.propidle.PropertyValue.propertyValue;
import static com.googlecode.propidle.indexing.LucenePropertiesIndexer.*;
import static com.googlecode.propidle.search.SearchResult.searchResult;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.io.Url.url;

public class LucenePropertiesSearcher implements PropertiesSearcher {
    private final Directory directory;
    private final LuceneSearcher luceneSearcher;

    public LucenePropertiesSearcher(Directory directory, Analyzer analyzer, Version version) {
        this.directory = directory;
        this.luceneSearcher = new LuceneSearcher(analyzer, version);
    }

    public Iterable<SearchResult> search(Query query) {
        if(query.isEmpty()) return empty();
        Query revisedQuery = query.againstFields(ALL_FIELDS);
        return luceneSearcher.search(directory, revisedQuery, toSearchResult());
    }

    private Callable1<? super Document, SearchResult> toSearchResult() {
        return new Callable1<Document, SearchResult>() {
            public SearchResult call(Document document) throws Exception {
                return searchResult(
                        url(document.get(URL)),
                        propertyName(document.get(PROPERTY_NAME)),
                        propertyValue(document.get(PROPERTY_VALUE)));
            }
        };
    }
}
