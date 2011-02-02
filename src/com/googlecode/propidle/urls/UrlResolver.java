package com.googlecode.propidle.urls;

import com.googlecode.propidle.PropertiesPath;
import com.googlecode.utterlyidle.io.Url;

public interface UrlResolver {
    Url resolvePropertiesUrl(PropertiesPath path);
    Url resolveFileNameUrl(PropertiesPath path);
    Url resolve(Url url);
}