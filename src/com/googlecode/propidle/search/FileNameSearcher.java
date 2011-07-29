package com.googlecode.propidle.search;

import com.googlecode.propidle.PathType;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.totallylazy.Pair;

public interface FileNameSearcher extends Searcher<Pair<PropertiesPath, PathType>>{
    Iterable<Pair<PropertiesPath, PathType>> childrenOf(PropertiesPath path);
}
