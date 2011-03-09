package com.googlecode.propidle.search;

import static com.googlecode.propidle.indexing.LucenePropertiesIndex.*;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.propidle.search.SearchResult.searchResult;
import com.googlecode.totallylazy.Callable1;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class LucenePropertiesSearcher implements PropertiesSearcher {
    private final Directory directory;
    private final LuceneSearcher luceneSearcher;

    public LucenePropertiesSearcher(Directory directory, Analyzer analyzer, Version version) {
        this.directory = directory;
        this.luceneSearcher = new LuceneSearcher(analyzer, version);
    }

    public Iterable<SearchResult> search(Query query) {
        if (query.isEmpty()) return empty();
        Query revisedQuery = query.againstFields(ALL_FIELDS);
        return sequence(luceneSearcher.search(directory, revisedQuery)).map(toSearchResult());
    }

    private Callable1<? super Document, SearchResult> toSearchResult() {
        return new Callable1<Document, SearchResult>() {
            public SearchResult call(Document document) throws Exception {
                return searchResult(
                        propertiesPath(document.get(PATH)),
                        propertyName(document.get(PROPERTY_NAME)),
                        propertyValue(document.get(PROPERTY_VALUE)));
            }
        };
    }
}
