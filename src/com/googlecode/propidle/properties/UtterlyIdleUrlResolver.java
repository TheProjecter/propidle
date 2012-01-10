package com.googlecode.propidle.properties;

import com.googlecode.propidle.filenames.FileNamesResource;
import com.googlecode.propidle.urls.UrlResolver;
import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.Redirector;

import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;

public class UtterlyIdleUrlResolver implements UrlResolver {
    private final Redirector redirector;

    public UtterlyIdleUrlResolver(Redirector redirector) {
        this.redirector = redirector;
    }

    public Uri resolvePropertiesUrl(PropertiesPath path) {
        return redirector.absoluteUriOf(method(on(PropertiesResource.class).getProperties(path)));
    }

    public Uri createPropertiesUrl() {
        return redirector.absoluteUriOf(method(on(PropertiesResource.class).getAll()));
    }

    public Uri resolveFileNameUrl(PropertiesPath path) {
        return redirector.absoluteUriOf(method(on(FileNamesResource.class).getChildrenOf(path)));
    }

    public Uri searchUrl() {
        return redirector.absoluteUriOf(method(on(FileNamesResource.class).getBase()));
    }
}
