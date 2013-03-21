package com.googlecode.propidle.authorisation;

import com.googlecode.utterlyidle.Request;

public interface Authoriser {
    boolean authorise(Request request);
}
