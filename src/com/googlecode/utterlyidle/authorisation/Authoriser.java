package com.googlecode.utterlyidle.authorisation;

import com.googlecode.utterlyidle.Request;

public interface Authoriser {
    boolean authorise(Request request);
}
