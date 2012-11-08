package com.googlecode.propidle.search;

import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
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

import static com.googlecode.propidle.client.properties.Properties.toPairs;
import static com.googlecode.propidle.util.Strings.reduceToAlphaNumerics;
import static com.googlecode.totallylazy.Sequences.sequence;

public class LucenePropertiesIndex implements PropertiesIndex {
    public static final String PATH = "url";
    public static final String SEARCHABLE_PROPERTY_NAME = "searchable.property.name";
    public static final String PROPERTY_NAME = "property.name";
    public static final String PROPERTY_VALUE = "property.value";
    public static final String SEARCHABLE_PROPERTY_VALUE = "searchable.property.value";
    public static final String SEARCHABLE_LOWER_CASE_PROPERTY_NAME = "searchable.lower.case.property.name";
    public static final Sequence<String> ALL_FIELDS = sequence(PATH, SEARCHABLE_PROPERTY_NAME, PROPERTY_NAME, SEARCHABLE_PROPERTY_VALUE, PROPERTY_VALUE, SEARCHABLE_LOWER_CASE_PROPERTY_NAME);

    private final IndexWriter writer;
    private final QueryParser queryParser;

    public LucenePropertiesIndex(IndexWriter writer, Version version) {
        this.writer = writer;
        this.queryParser = new QueryParser(version, PATH, new KeywordAnalyzer());
    }

    public void set(Pair<PropertiesPath, Properties> pathAndProperties) {
        PropertiesPath path = pathAndProperties.first();
        Properties properties = pathAndProperties.second();

        try {
            Sequence<Document> documentPerField = toPairs(properties).map(documentForIndividualProperty(path));

            writer.deleteDocuments(existingDocument(path));

            documentPerField.fold(writer, updateProperties());
        } catch (Exception e) {
            throw new RuntimeException("Could not index " + path, e);
        }
    }

    private Query existingDocument(PropertiesPath path) throws ParseException {
        return queryParser.parse(String.format("%s:\"%s\"", PATH, path));
    }

    private Callable2<? super IndexWriter, ? super Document, IndexWriter> updateProperties() {
        return new Callable2<IndexWriter, Document, IndexWriter>() {
            public IndexWriter call(IndexWriter writer, Document document) throws Exception {
                writer.addDocument(document);
                return writer;
            }
        };
    }

    private Callable1<? super Pair<String, String>, Document> documentForIndividualProperty(final PropertiesPath path) {
        return new Callable1<Pair<String, String>, Document>() {
            public Document call(Pair<String, String> property) throws Exception {
                Document document = new Document();
                document.add(pathField(path));
                document.add(propertyNameField(property));
                document.add(searchablePropertyName(property));
                document.add(propertyValueField(property));
                document.add(searchablePropertyValueField(property));
                document.add(searchableLowerCasePropertyName(property));
                return document;
            }
        };
    }

    private Fieldable pathField(PropertiesPath path) {
        return new Field(PATH, path.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED);
    }

    private Field propertyNameField(Pair<String, String> property) {
        return new Field(PROPERTY_NAME, property.first(), Field.Store.YES, Field.Index.NOT_ANALYZED);
    }

    private Field propertyValueField(Pair<String, String> property) {
        return new Field(PROPERTY_VALUE, property.second(), Field.Store.YES, Field.Index.NOT_ANALYZED);
    }

    private Field searchablePropertyValueField(Pair<String, String> property) {
        return new Field(SEARCHABLE_PROPERTY_VALUE, reduceToAlphaNumerics(property.second()), Field.Store.NO, Field.Index.ANALYZED);
    }

    private Fieldable searchableLowerCasePropertyName(Pair<String, String> property) {
        return new Field(SEARCHABLE_LOWER_CASE_PROPERTY_NAME, property.first().toLowerCase(), Field.Store.NO, Field.Index.NOT_ANALYZED);
    }

    private Fieldable searchablePropertyName(Pair<String, String> property) {
        return new Field(SEARCHABLE_PROPERTY_NAME, reduceToAlphaNumerics(property.first()), Field.Store.NO, Field.Index.ANALYZED);
    }

}
