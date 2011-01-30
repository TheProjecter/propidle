package com.googlecode.propidle.versioncontrol.revisions;

import com.googlecode.propidle.persistence.RecordLock;

public class LockHighestRevisionNumbersDecorator implements HighestRevisionNumbers {
    private final HighestRevisionNumbers decorated;
    private final RecordLock recordLock;

    public LockHighestRevisionNumbersDecorator(HighestRevisionNumbers decorated, RecordLock recordLock) {
        this.decorated = decorated;
        this.recordLock = recordLock;
    }

    public HighestExistingRevisionNumber highestExistingRevision() {
        return decorated.highestExistingRevision();
    }

    public NewRevisionNumber newRevisionNumber() {
        recordLock.aquire(HighestRevisionNumbersFromRecords.HIGHEST_REVISION);
        return decorated.newRevisionNumber();
    }
}
