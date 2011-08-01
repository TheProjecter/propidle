package com.googlecode.propidle.server;

import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Record;

import java.io.PrintWriter;
import java.util.Properties;

public interface IndexRebuilder {
    public void index(Iterable<Pair<PropertiesPath,Properties>> recordsToIndex, PrintWriter writer);
}
