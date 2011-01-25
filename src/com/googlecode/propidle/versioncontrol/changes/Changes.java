package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.PropertyComparison;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;

public interface Changes {
    Iterable<Change> get(PropertiesPath propertiesPath);
    Changes put(PropertiesPath path, Iterable<PropertyComparison> changes);

    Iterable<Change> get(PropertiesPath propertiesPath, RevisionNumber revisionNumber);
}
