package com.googlecode.propidle;

import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;

import java.util.Properties;

public interface AllProperties {
    Properties get(PropertiesPath path);
    Properties getAtRevision(PropertiesPath path, RevisionNumber revision);
    AllProperties put(PropertiesPath path, java.util.Properties properties);
}
