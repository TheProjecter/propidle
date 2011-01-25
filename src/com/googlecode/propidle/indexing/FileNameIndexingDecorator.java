package com.googlecode.propidle.indexing;

import com.googlecode.propidle.AllProperties;
import com.googlecode.propidle.PropertiesPath;

import java.util.Properties;

public class FileNameIndexingDecorator implements AllProperties {
    private final AllProperties decorated;
    private final FileNameIndexer indexer;

    public FileNameIndexingDecorator(AllProperties decorated, FileNameIndexer indexer) {
        this.decorated = decorated;
        this.indexer = indexer;
    }

    public Properties get(PropertiesPath path) {
        return decorated.get(path);
    }

    public AllProperties put(PropertiesPath path, Properties properties) {
        indexer.index(path);
        decorated.put(path, properties);
        return this;
    }
}
