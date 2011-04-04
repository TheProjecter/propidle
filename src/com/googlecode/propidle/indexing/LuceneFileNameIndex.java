package com.googlecode.propidle.indexing;

import com.googlecode.propidle.PathType;
import com.googlecode.propidle.properties.PropertiesPath;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import static com.googlecode.propidle.PathType.DIRECTORY;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.util.Strings.reduceToAlphaNumerics;
import static com.googlecode.totallylazy.Sequences.sequence;

public class LuceneFileNameIndex implements FileNameIndex {
    public static final String PATH = "path";
    public static final String PATH_TYPE = "path.type";
    public static final String PARENT = "parent";
    public static final String SEARCHABLE_PATH = "searchable.path";
    public static final Iterable<String> ALL_FIELDS = sequence(PATH, PARENT, SEARCHABLE_PATH);

    private final IndexWriter writer;
    private final QueryParser queryParser;

    public LuceneFileNameIndex(IndexWriter writer, Version version) {
        this.writer = writer;
        this.queryParser = new QueryParser(version, PATH, new KeywordAnalyzer());
    }

    public void set(PropertiesPath path) {
        indexIncludingParents(path, PathType.FILE);
    }

    private void indexIncludingParents(PropertiesPath path, PathType pathType) {
        if (path.parent().equals(path)) {
            // Do nothing for root node
        } else{
            indexIncludingParents(propertiesPath(path.parent().toString()), DIRECTORY);
            index(path, pathType);
        }
    }

    private void index(PropertiesPath path, PathType pathType) {
        try {
            writer.deleteDocuments(existingDocument(path, pathType));

            Document document = documentFor(path, pathType);
            writer.addDocument(document);
        } catch (Exception e) {
            throw new RuntimeException("Could not index " + path, e);
        }
    }

    private Query existingDocument(PropertiesPath path, PathType pathType) throws ParseException {
        return queryParser.parse(String.format("%s:\"%s\" AND %s:\"%s\"", PATH, path, PATH_TYPE, pathType.name().toLowerCase()));
    }

    private Document documentFor(PropertiesPath path, PathType pathType) {
        Document document = new Document();
        document.add(parentField(path));
        document.add(valueField(path));
        document.add(searchableField(path));
        document.add(pathTypeField(pathType));
        return document;
    }

    private Fieldable valueField(PropertiesPath path) {
        return new Field(PATH, path.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED);
    }

    private Fieldable parentField(PropertiesPath path) {
        return new Field(PARENT, propertiesPath(path.parent().toString()).toString(), Field.Store.YES, Field.Index.NOT_ANALYZED);
    }

    private Fieldable searchableField(PropertiesPath path) {
        return new Field(SEARCHABLE_PATH, reduceToAlphaNumerics(path.toString()), Field.Store.YES, Field.Index.ANALYZED);
    }

    private Fieldable pathTypeField(PathType pathType) {
        return new Field(PATH_TYPE, pathType.name(), Field.Store.YES, Field.Index.ANALYZED);
    }
}
