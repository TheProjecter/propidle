package com.googlecode.propidle.server.properties;

import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.server.filenames.FileNamesResource;
import com.googlecode.propidle.urls.UrlResolver;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.io.Url;

import static com.googlecode.utterlyidle.io.HierarchicalPath.hierarchicalPath;
import static com.googlecode.utterlyidle.io.Url.url;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
import static com.googlecode.utterlyidle.proxy.Resource.urlOf;

public class UtterlyIdleUrlResolver implements UrlResolver {
    private final BasePath basePath;

    public UtterlyIdleUrlResolver(BasePath basePath) {
        this.basePath = basePath;
    }

    public Url resolvePropertiesUrl(PropertiesPath path) {
        return prefixWithBasePath(urlOf(resource(PropertiesResource.class).getProperties(path)));
    }

    public Url resolveFileNameUrl(PropertiesPath path) {
        return prefixWithBasePath(urlOf(resource(FileNamesResource.class).getChildrenOf(path)));
    }

    public Url resolve(Url url) {
        if(url.toURI().getScheme() == null) {
            return url.replacePath(hierarchicalPath(basePath.toString() + url.path()));
        }
        return url;
    }

    private Url prefixWithBasePath(String url) {
        return url(basePath.toString() + url);
    }
}
