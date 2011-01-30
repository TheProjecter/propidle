package com.googlecode.propidle;

import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;

import java.util.Properties;

public interface AllProperties {
    Properties get(PropertiesPath path, RevisionNumber revision);
    RevisionNumber put(PropertiesPath path, java.util.Properties properties);
}
