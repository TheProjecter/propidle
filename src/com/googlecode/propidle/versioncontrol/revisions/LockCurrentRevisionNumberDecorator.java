package com.googlecode.propidle.versioncontrol.revisions;

import com.googlecode.propidle.persistence.RecordLock;

public class LockCurrentRevisionNumberDecorator implements CurrentRevisionNumber {
    private final CurrentRevisionNumber decorated;
    private final RecordLock recordLock;

    public LockCurrentRevisionNumberDecorator(CurrentRevisionNumber decorated, RecordLock recordLock) {
        this.decorated = decorated;
        this.recordLock = recordLock;
    }

    public RevisionNumber current() {
        recordLock.aquire(CurrentRevisionNumberFromRecords.HIGHEST_REVISION);
        return decorated.current();
    }
}
