package com.googlecode.propidle.indexing;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.concurrent.Callable;

public class IndexWriterActivator implements Callable<IndexWriter> {
    private final Directory directory;
    private final Analyzer analyzer;

    public IndexWriterActivator(Directory directory, Analyzer analyzer) {
        this.directory = directory;
        this.analyzer = analyzer;
    }

    public IndexWriter call()  {
        return indexWriter(directory, analyzer);
    }

    public static IndexWriter indexWriter(Directory directory, Analyzer analyzer) {
        try {
            return new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create IndexWriter", e);
        }
    }
}
