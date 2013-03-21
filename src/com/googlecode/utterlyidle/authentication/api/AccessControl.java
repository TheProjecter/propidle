package com.googlecode.utterlyidle.authentication.api;

import com.googlecode.utterlyidle.Request;

public interface AccessControl {
    boolean requiresAuthentication(Request request);
}
