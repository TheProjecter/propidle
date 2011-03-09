package com.googlecode.propidle.versioncontrol.revisions;

import java.util.concurrent.Callable;

public class HighestExistingRevisionNumberActivator implements Callable<HighestExistingRevisionNumber> {
    private final HighestRevisionNumbers highestRevisionNumbers;

    public HighestExistingRevisionNumberActivator(HighestRevisionNumbers highestRevisionNumbers) {
        this.highestRevisionNumbers = highestRevisionNumbers;
    }

    public HighestExistingRevisionNumber call() throws Exception {
        return highestRevisionNumbers.highestExistingRevision();
    }
}
