package com.googlecode.propidle.versioncontrol.revisions;

public class NewRevisionNumber extends RevisionNumber{
    public static NewRevisionNumber newRevisionNumber(Integer value) {
        return new NewRevisionNumber(value);
    }

    public static NewRevisionNumber newRevisionNumber(RevisionNumber value) {
        return newRevisionNumber(value.value());
    }

    protected NewRevisionNumber(Integer value) {
        super(value);
    }
}