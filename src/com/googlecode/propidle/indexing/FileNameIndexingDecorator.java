package com.googlecode.propidle.indexing;

import com.googlecode.propidle.properties.AllProperties;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;

import java.util.Properties;

public class FileNameIndexingDecorator implements AllProperties {
    private final AllProperties decorated;
    private final FileNameIndexer indexer;

    public FileNameIndexingDecorator(AllProperties decorated, FileNameIndexer indexer) {
        this.decorated = decorated;
        this.indexer = indexer;
    }

    public Properties get(PropertiesPath path, RevisionNumber revision) {
        return decorated.get(path, revision);
    }

    public RevisionNumber put(PropertiesPath path, Properties properties) {
        indexer.index(path);
        return decorated.put(path, properties);
    }
}
