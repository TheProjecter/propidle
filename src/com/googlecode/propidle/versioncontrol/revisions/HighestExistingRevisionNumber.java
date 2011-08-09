package com.googlecode.propidle.versioncontrol.revisions;

public class HighestExistingRevisionNumber extends RevisionNumber{
    public static HighestExistingRevisionNumber highestExistingRevisionNumber(Integer value) {
        return new HighestExistingRevisionNumber(value);
    }

    protected HighestExistingRevisionNumber(Integer value) {
        super(value);
    }
}
