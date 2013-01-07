package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.lazyrecords.Records;
import com.googlecode.lazyrecords.sql.SqlRecords;

import java.util.concurrent.Callable;

public class ChildPathsActivator implements Callable<ChildPaths> {
    private final Records records;

    public ChildPathsActivator(Records records) {
        this.records = records;
    }

    @Override
    public ChildPaths call() throws Exception {
        return records instanceof SqlRecords? new ChildPathsFromSql((SqlRecords) records) : new ChildPathsFromRecords(records);
    }
}
