package com.googlecode.propidle.indexing;

import com.googlecode.propidle.search.LuceneIndexWriterTransaction;
import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class LuceneModule implements ApplicationScopedModule, RequestScopedModule{
    private final Directory directory;

    public LuceneModule(Directory directory) {
        if (directory == null) throw new NullArgumentException("directory");
        this.directory = directory;
    }

    public Module addPerApplicationObjects(Container container) {
        container.addInstance(Version.class, Version.LUCENE_30);
        container.addInstance(Directory.class, directory);
        container.addInstance(Analyzer.class, new StandardAnalyzer(Version.LUCENE_30));
        container.addActivator(IndexWriter.class, IndexWriterActivator.class);
        return this;
    }

    public Module addPerRequestObjects(Container container) {
        container.decorate(HttpHandler.class, LuceneIndexWriterTransaction.class);
        return this;
    }
}
