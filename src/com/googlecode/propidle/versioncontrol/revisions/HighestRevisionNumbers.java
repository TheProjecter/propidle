package com.googlecode.propidle.versioncontrol.revisions;

public interface HighestRevisionNumbers {
    public NewRevisionNumber newRevisionNumber();
    public HighestExistingRevisionNumber highestExistingRevision();
}
