package com.googlecode.propidle.search;

import com.googlecode.propidle.indexing.Index;
import com.googlecode.totallylazy.Pair;
import com.googlecode.propidle.properties.PropertiesPath;

import java.util.Properties;

public interface PropertiesIndex extends Index<Pair<PropertiesPath, Properties>> {
}
