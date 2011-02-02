package com.googlecode.propidle.server;

import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;

import static java.lang.Integer.parseInt;

public class RequestedRevisionNumber extends RevisionNumber {
    public static RequestedRevisionNumber requestedRevisionNumber(String value) {
        return requestedRevisionNumber(parseInt(value));
    }

    public static RequestedRevisionNumber requestedRevisionNumber(RevisionNumber value) {
        return requestedRevisionNumber(value.value());
    }
    public static RequestedRevisionNumber requestedRevisionNumber(int value) {
        return new RequestedRevisionNumber(value);
    }

    private RequestedRevisionNumber(int value) {
        super(value);
    }
}
