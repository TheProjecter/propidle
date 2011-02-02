package com.googlecode.propidle.indexing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.io.Url;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import java.util.Properties;

import static com.googlecode.propidle.Properties.toPairs;
import static com.googlecode.propidle.util.Strings.reduceToAlphaNumerics;
import static com.googlecode.totallylazy.Sequences.sequence;

public class LucenePropertiesIndexer implements PropertiesIndexer {
    public static final String URL = "url";
    public static final String SEARCHABLE_PROPERTY_NAME = "searchable.property.name";
    public static final String PROPERTY_NAME = "property.name";
    public static final String PROPERTY_VALUE = "property.value";
    public static final String SEARCHABLE_PROPERTY_VALUE = "searchable.property.value";
    public static final Sequence<String> ALL_FIELDS = sequence(URL, SEARCHABLE_PROPERTY_NAME, PROPERTY_NAME, SEARCHABLE_PROPERTY_VALUE, PROPERTY_VALUE);

    private final IndexWriter writer;
    private final QueryParser queryParser;

    public LucenePropertiesIndexer(IndexWriter writer, Version version) {
        this.writer = writer;
        this.queryParser = new QueryParser(version, URL, new KeywordAnalyzer());
    }

    public void index(Pair<Url, Properties> urlAndProperties) {
        Url url = urlAndProperties.first();
        Properties properties = urlAndProperties.second();

        try {
            Sequence<Document> documentPerField = toPairs(properties).map(documentForIndividualProperty(url));

            writer.deleteDocuments(existingDocument(url));

            documentPerField.fold(writer, updateProperties());
        } catch (Exception e) {
            throw new RuntimeException("Could not index " + url, e);
        }
    }

    private Query existingDocument(Url url) throws ParseException {
        return queryParser.parse(String.format("%s:\"%s\"", URL, url));
    }

    private Callable2<? super IndexWriter, ? super Document, IndexWriter> updateProperties() {
        return new Callable2<IndexWriter, Document, IndexWriter>() {
            public IndexWriter call(IndexWriter writer, Document document) throws Exception {
                writer.addDocument(document);
                return writer;
            }
        };
    }

    private Callable1<? super Pair<String, String>, Document> documentForIndividualProperty(final Url url) {
        return new Callable1<Pair<String, String>, Document>() {
            public Document call(Pair<String, String> property) throws Exception {
                Document document = new Document();
                document.add(urlField(url));
                document.add(propertyNameField(property));
                document.add(searchablePropertyName(property));
                document.add(propertyValueField(property));
                document.add(searchablePropertyValueField(property));
                return document;
            }
        };
    }

    private Fieldable urlField(Url url) {
        return new Field(URL, url.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED);
    }

    private Field propertyNameField(Pair<String, String> property) {
        return new Field(PROPERTY_NAME, property.first(), Field.Store.YES, Field.Index.NOT_ANALYZED);
    }

    private Field searchablePropertyValueField(Pair<String, String> property) {
        return new Field(SEARCHABLE_PROPERTY_VALUE, reduceToAlphaNumerics(property.second()), Field.Store.NO, Field.Index.ANALYZED);
    }

    private Field propertyValueField(Pair<String, String> property) {
        return new Field(PROPERTY_VALUE, property.second(), Field.Store.YES, Field.Index.NOT_ANALYZED);
    }

    private Fieldable searchablePropertyName(Pair<String, String> property) {
        return new Field(SEARCHABLE_PROPERTY_NAME, reduceToAlphaNumerics(property.first()), Field.Store.NO, Field.Index.ANALYZED);
    }

}
