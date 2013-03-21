package com.googlecode.propidle.authentication.api;

import com.googlecode.utterlyidle.Request;

public interface AccessControl {
    boolean requiresAuthentication(Request request);
}
