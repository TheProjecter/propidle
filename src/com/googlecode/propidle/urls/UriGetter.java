package com.googlecode.propidle.urls;

import java.io.InputStream;
import java.net.URI;

public interface UriGetter {
    public InputStream get(URI uri, MimeType mimeType) throws Exception;
}
