package com.googlecode.propidle.server;

import com.googlecode.propidle.filenames.FileNameIndex;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.search.PropertiesIndex;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.io.PrintWriter;
import java.util.Properties;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.TimeCallable.calculateMilliseconds;
import static java.lang.System.nanoTime;

public class FileAndPropertiesIndexRebuilder implements IndexRebuilder {
    private final FileNameIndex fileNameIndexer;
    private final PropertiesIndex propertiesIndexer;

    public FileAndPropertiesIndexRebuilder(FileNameIndex fileNameIndexer, PropertiesIndex propertiesIndexer) {
        this.fileNameIndexer = fileNameIndexer;
        this.propertiesIndexer = propertiesIndexer;
    }

    public void index(Iterable<Pair<PropertiesPath, Properties>> properties, PrintWriter writer) {
        long start = nanoTime();
        sequence(properties).fold(fileNameIndexer, indexFileName());
        sequence(properties).fold(propertiesIndexer, indexProperties());
        writer.println(String.format("Indexing finished in %s milliseconds \n", (calculateMilliseconds(start, nanoTime()))));
    }

    private static Callable2<? super PropertiesIndex, ? super Pair<PropertiesPath, Properties>, PropertiesIndex> indexProperties() {
        return new Callable2<PropertiesIndex, Pair<PropertiesPath, Properties>, PropertiesIndex>() {
            public PropertiesIndex call(PropertiesIndex propertiesIndexer, Pair<PropertiesPath, Properties> pathAndProperties) throws Exception {
                propertiesIndexer.set(pathAndProperties);
                return propertiesIndexer;
            }
        };
    }

    private static Callable2<? super FileNameIndex, ? super Pair<PropertiesPath, Properties>, FileNameIndex> indexFileName() {
        return new Callable2<FileNameIndex, Pair<PropertiesPath, Properties>, FileNameIndex>() {
            public FileNameIndex call(FileNameIndex fileNameIndexer, Pair<PropertiesPath, Properties> pathAndProperties) throws Exception {
                fileNameIndexer.set(pathAndProperties.first());
                return fileNameIndexer;
            }
        };
    }

}
