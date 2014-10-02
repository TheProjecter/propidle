package com.googlecode.propidle.filenames;

import com.googlecode.propidle.properties.AllProperties;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Option;

import java.util.Properties;

public class FileNameIndexingDecorator implements AllProperties {
    private final AllProperties decorated;
    private final FileNameIndex indexer;

    public FileNameIndexingDecorator(AllProperties decorated, FileNameIndex indexer) {
        this.decorated = decorated;
        this.indexer = indexer;
    }

    @Override
    public Properties get(PropertiesPath path, Option<? extends RevisionNumber> revision) {
        return decorated.get(path, revision);
    }

    public RevisionNumber put(PropertiesPath path, Properties properties) {
        indexer.set(path);
        return decorated.put(path, properties);
    }
}
