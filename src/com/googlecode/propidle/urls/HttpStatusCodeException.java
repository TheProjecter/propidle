package com.googlecode.propidle.urls;

import com.googlecode.utterlyidle.Status;

import java.net.URI;

public class HttpStatusCodeException extends RuntimeException{
    private final URI uri;
    private final Status status;
    private final String content;
    private static final String LINE = "------------------------------------------";

    public HttpStatusCodeException(URI uri, Status status, String content){
        super(String.format("Getting url '%s' resulted in %s with content:\n%s\n%s", uri, status, content, LINE));
        this.uri = uri;
        this.status = status;
        this.content = content;
    }

    public Status statusCode() {
        return status;
    }

    public URI uri() {
        return uri;
    }

    public Status status() {
        return status;
    }

    public String content() {
        return content;
    }
}
