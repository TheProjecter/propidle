package com.googlecode.propidle.properties;

import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Option;

import java.util.Properties;

public interface AllProperties {
    Properties get(PropertiesPath path, Option<? extends RevisionNumber> revision);
    RevisionNumber put(PropertiesPath path, java.util.Properties properties);
}
