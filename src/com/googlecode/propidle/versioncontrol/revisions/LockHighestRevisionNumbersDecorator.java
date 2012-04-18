package com.googlecode.propidle.versioncontrol.revisions;

import com.googlecode.utterlyidle.migrations.persistence.RecordLock;

import static com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords.HIGHEST_REVISION;

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
        recordLock.aquire(HIGHEST_REVISION);
        return decorated.newRevisionNumber();
    }
}
