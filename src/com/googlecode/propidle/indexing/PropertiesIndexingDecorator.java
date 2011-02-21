package com.googlecode.propidle.indexing;

import com.googlecode.propidle.properties.AllProperties;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;

import java.util.Properties;

import static com.googlecode.totallylazy.Pair.pair;

public class PropertiesIndexingDecorator implements AllProperties {
    private final AllProperties decorated;
    private final PropertiesIndex indexer;

    public PropertiesIndexingDecorator(AllProperties decorated, PropertiesIndex indexer) {
        this.decorated = decorated;
        this.indexer = indexer;
    }

    public Properties get(PropertiesPath path, RevisionNumber revision) {
        return decorated.get(path, revision);
    }

    public RevisionNumber put(PropertiesPath path, Properties properties) {
        indexer.set(pair(path, properties));
        return decorated.put(path, properties);
    }
}
