package com.googlecode.propidle.status;

public interface StatusCheck {
    public StatusCheckResult check() throws Exception;
}
