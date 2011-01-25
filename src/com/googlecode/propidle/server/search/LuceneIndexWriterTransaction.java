package com.googlecode.propidle.server.search;

import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import org.apache.lucene.index.IndexWriter;

public class LuceneIndexWriterTransaction implements HttpHandler {
    private final HttpHandler decorated;
    private final IndexWriter indexWriter;

    public LuceneIndexWriterTransaction(HttpHandler decorated, IndexWriter indexWriter) {
        this.decorated = decorated;
        this.indexWriter = indexWriter;
    }

    public void handle(Request request, Response response) throws Exception {
        decorated.handle(request, response);
        indexWriter.commit();
    }
}
