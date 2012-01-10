package com.googlecode.propidle.urls;

import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.totallylazy.Uri;

public interface UrlResolver {
    Uri resolvePropertiesUrl(PropertiesPath path);

    Uri resolveFileNameUrl(PropertiesPath path);

    Uri searchUrl();

    Uri createPropertiesUrl();
}