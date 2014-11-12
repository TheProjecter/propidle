package com.googlecode.propidle.versioncontrol.revisions;

import com.googlecode.propidle.indexing.Index;
import com.googlecode.totallylazy.Option;

public interface HighestRevisionIndex extends Index<HighestExistingRevisionNumber> {
    public Option<HighestExistingRevisionNumber> get();
}
