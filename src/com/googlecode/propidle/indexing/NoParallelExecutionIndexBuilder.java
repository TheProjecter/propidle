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

    private final ParallelExecutionGuard parallelExecutionGuard;
    private final IndexRebuilder indexRebuilder;

    public NoParallelExecutionIndexBuilder(ParallelExecutionGuard parallelExecutionGuard, IndexRebuilder indexRebuilder) {
        this.parallelExecutionGuard = parallelExecutionGuard;
        this.indexRebuilder = indexRebuilder;
    }

    public void index(final Iterable<Pair<PropertiesPath, Properties>> recordsToIndex, final PrintWriter writer) {
        if(!parallelExecutionGuard.execute(rebuildIndex(recordsToIndex, writer))) {
            writer.println("Index is already rebuilding.");
        }
    }

    private Callable<Void> rebuildIndex(final Iterable<Pair<PropertiesPath, Properties>> recordsToIndex, final PrintWriter writer) {
        return new Callable<Void>() {
            public Void call() throws Exception {
                indexRebuilder.index(recordsToIndex, writer);
                return null;
            }
        };
    }
}
