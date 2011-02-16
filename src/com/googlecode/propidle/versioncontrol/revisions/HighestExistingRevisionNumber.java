package com.googlecode.propidle.versioncontrol.revisions;

public class HighestExistingRevisionNumber extends RevisionNumber{
    public static HighestExistingRevisionNumber highestExistingRevisionNumber(RevisionNumber value) {
        return highestExistingRevisionNumber(value.value());
    }
    public static HighestExistingRevisionNumber highestExistingRevisionNumber(Number value) {
        return new HighestExistingRevisionNumber(value.intValue());
    }

    protected HighestExistingRevisionNumber(Integer value) {
        super(value);
    }
}
