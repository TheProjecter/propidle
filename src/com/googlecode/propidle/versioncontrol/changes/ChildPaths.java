package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.totallylazy.Sequence;

public interface ChildPaths {
    Sequence<PropertiesPath> childPaths(PropertiesPath parent);
}
