package com.googlecode.propidle.indexing;

import com.googlecode.propidle.ApplicationStartupBarrier;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.propidle.search.LuceneIndexWriterTransaction;
import com.googlecode.propidle.server.FileAndPropertiesIndexRebuilder;
import com.googlecode.propidle.server.IndexRebuilder;
import com.googlecode.propidle.server.Scheduler;
import com.googlecode.propidle.server.SchedulerModule;
import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.propidle.util.ParallelExecutionGuard;
import com.googlecode.utterlyidle.HttpHandler;
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

import java.util.concurrent.CountDownLatch;

import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;

public class LuceneModule implements ApplicationScopedModule, RequestScopedModule, SchedulerModule, ResourcesModule {
    private static final PropertyName LUCENE_INDEX_REFRESH_TIME_PROPERTY_NAME = propertyName("lucene.index.refresh.time.in.minutes");
    private final Directory directory;
    private final CountDownLatch startLatch;

    public LuceneModule(Directory directory, CountDownLatch startLatch) {
        this.startLatch = startLatch;
        if (directory == null) throw new NullArgumentException("directory");
        this.directory = directory;
    }

    public Module addPerApplicationObjects(Container container) {
        container.addInstance(ApplicationStartupBarrier.class, new ApplicationStartupBarrier(startLatch));
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

    public void addTo(Scheduler scheduler) {
        scheduler.schedule(RefreshLuceneIndex.class, LUCENE_INDEX_REFRESH_TIME_PROPERTY_NAME);
    }

    public Module addResources(Resources resources) {
        resources.add(annotatedClass(RebuildIndexResource.class));
        return this;
    }

}
