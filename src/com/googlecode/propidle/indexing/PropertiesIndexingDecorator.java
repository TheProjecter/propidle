package com.googlecode.propidle.indexing;

import com.googlecode.propidle.AllProperties;
import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.urls.UrlResolver;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;

import java.util.Properties;

import static com.googlecode.totallylazy.Pair.pair;

public class PropertiesIndexingDecorator implements AllProperties {
    private final AllProperties decorated;
    private final PropertiesIndexer indexer;
    private final UrlResolver urlResolver;

    public PropertiesIndexingDecorator(AllProperties decorated, PropertiesIndexer indexer, UrlResolver urlResolver) {
        this.decorated = decorated;
        this.indexer = indexer;
        this.urlResolver = urlResolver;
    }

    public Properties get(PropertiesPath path) {
        return decorated.get(path);
    }

    public Properties getAtRevision(PropertiesPath path, RevisionNumber revision) {
        return decorated.getAtRevision(path, revision);
    }

    public AllProperties put(PropertiesPath path, Properties properties) {
        indexer.index(pair(urlResolver.resolvePropertiesUrl(path), properties));
        decorated.put(path, properties);
        return this;
    }
}
