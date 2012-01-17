package com.googlecode.propidle;

import com.googlecode.totallylazy.proxy.Invocation;
import com.googlecode.utterlyidle.Redirector;

public class PropidlePath {
    private final Redirector redirector;

    public PropidlePath(Redirector redirector) {
        this.redirector = redirector;
    }

    public String path(Invocation method) {
        return "/"+redirector.resourceUriOf(method).toString();
    }

    public String absoluteUriOf(Invocation method) {
        return redirector.absoluteUriOf(method).toString();
    }
}
