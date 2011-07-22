package com.googlecode.propidle.indexing;

import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.server.IndexRebuilder;
import com.googlecode.propidle.util.ParallelExecutionGuard;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.io.PrintWriter;
import java.util.Properties;
import java.util.concurrent.Callable;

public class NoParallelExecutionIndexBuilder implements IndexRebuilder {

    private final ParallelExecutionGuard parallelExecutionGuard = new ParallelExecutionGuard();
    private final IndexRebuilder indexRebuilder;

    public NoParallelExecutionIndexBuilder(IndexRebuilder indexRebuilder) {
        this.indexRebuilder = indexRebuilder;
    }

    public void index(final Sequence<Pair<PropertiesPath, Properties>> recordsToIndex, final PrintWriter writer) {
        if(!parallelExecutionGuard.execute(rebuildIndex(recordsToIndex, writer))) {
            writer.println("Index is already running.");
        }
    }

    private Callable<Void> rebuildIndex(final Sequence<Pair<PropertiesPath, Properties>> recordsToIndex, final PrintWriter writer) {
        return new Callable<Void>() {
            public Void call() throws Exception {
                indexRebuilder.index(recordsToIndex, writer);
                return null;
            }
        };
    }
}
