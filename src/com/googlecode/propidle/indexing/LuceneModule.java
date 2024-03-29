package com.googlecode.propidle.indexing;

import com.googlecode.propidle.scheduling.SchedulableRequestModule;
import com.googlecode.propidle.scheduling.SchedulableRequests;
import com.googlecode.propidle.search.LuceneIndexWriterTransaction;
import com.googlecode.propidle.server.FileAndPropertiesIndexRebuilder;
import com.googlecode.propidle.server.IndexRebuilder;
import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.propidle.util.ParallelExecutionGuard;
import com.googlecode.propidle.versioncontrol.revisions.HighestRevisionIndex;
import com.googlecode.propidle.versioncontrol.revisions.LuceneHighestRevisionIndex;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.yadic.Container;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static java.util.Collections.emptySet;

public class LuceneModule implements ApplicationScopedModule, RequestScopedModule, ResourcesModule, SchedulableRequestModule {
    public static final String REBUILD_INDEX_TASK_NAME = "rebuildIndex";
    private final Directory directory;

    public LuceneModule(Directory directory) {
        if (directory == null) throw new NullArgumentException("directory");
        this.directory = directory;
    }

    public Container addPerApplicationObjects(Container container) {
        container.addInstance(Version.class, Version.LUCENE_30);
        container.addInstance(Directory.class, directory);
        container.addInstance(Analyzer.class, new StandardAnalyzer(Version.LUCENE_30, emptySet()));
        container.addActivator(IndexWriter.class, IndexWriterActivator.class);
        return container.add(ParallelExecutionGuard.class);
    }

    public Container addPerRequestObjects(Container container) {
        container.add(IndexRebuilder.class, FileAndPropertiesIndexRebuilder.class);
        container.decorate(IndexRebuilder.class, NoParallelExecutionIndexBuilder.class);
        container.add(HighestRevisionIndex.class, LuceneHighestRevisionIndex.class);
        return container.decorate(HttpHandler.class, LuceneIndexWriterTransaction.class);
    }

    public Resources addResources(Resources resources) {
        return resources.add(annotatedClass(RebuildIndexResource.class));
    }

    public void addTask(SchedulableRequests schedulableRequests) {
        schedulableRequests.addTask(REBUILD_INDEX_TASK_NAME, post(RebuildIndexResource.NAME).build());
    }
}