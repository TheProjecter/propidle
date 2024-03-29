package com.googlecode.propidle.versioncontrol.revisions;

import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

public class RevisionsModule implements RequestScopedModule {
    public Container addPerRequestObjects(Container container) {
        container.addActivator(HighestExistingRevisionNumber.class, HighestExistingRevisionNumberActivator.class);

        container.add(HighestRevisionNumbers.class, HighestRevisionNumbersFromRecords.class);
        return container.decorate(HighestRevisionNumbers.class, LockHighestRevisionNumbersDecorator.class);
    }
}