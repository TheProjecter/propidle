package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.propidle.PathType;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;

public interface AllChanges {
    Iterable<Change> get(PropertiesPath propertiesPath);
    Iterable<Change> getLatestChanges(PropertiesPath propertiesPath);
    Iterable<com.googlecode.totallylazy.Pair<PropertiesPath,PathType>> childrenOf(PropertiesPath propertiesPath);
    Iterable<Change> get(PropertiesPath propertiesPath, RevisionNumber revisionNumber);
    AllChangesFromRecords put(Iterable<Change> changes);
}
