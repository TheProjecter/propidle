package com.googlecode.propidle.indexing;

import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.propidle.scheduling.SchedulableTask;
import com.googlecode.propidle.scheduling.SchedulableTaskModule;
import com.googlecode.propidle.scheduling.SchedulableTasks;
import com.googlecode.propidle.search.LuceneIndexWriterTransaction;
import com.googlecode.propidle.server.FileAndPropertiesIndexRebuilder;
import com.googlecode.propidle.server.IndexRebuilder;
import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.propidle.util.ParallelExecutionGuard;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.yadic.Container;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;

public class LuceneModule implements ApplicationScopedModule, RequestScopedModule, ResourcesModule, SchedulableTaskModule {
    private static final PropertyName LUCENE_INDEX_REFRESH_TIME_PROPERTY_NAME = propertyName("lucene.index.refresh.time.in.minutes");
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
        container.add(ParallelExecutionGuard.class);
        container.add(IndexRebuilder.class, FileAndPropertiesIndexRebuilder.class);
        container.decorate(IndexRebuilder.class, NoParallelExecutionIndexBuilder.class);
        return this;
    }

    public Module addPerRequestObjects(Container container) {
        container.decorate(HttpHandler.class, LuceneIndexWriterTransaction.class);
        return this;
    }

    public Module addResources(Resources resources) {
        resources.add(annotatedClass(RebuildIndexResource.class));
        return this;
    }

    public void addTask(SchedulableTasks tasks) {
        tasks.addTask(new SchedulableTask("rebuildIndex", RequestBuilder.post(RebuildIndexResource.NAME).build(), LUCENE_INDEX_REFRESH_TIME_PROPERTY_NAME));
    }
}
