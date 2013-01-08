package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.lazyrecords.Records;
import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.propidle.PersistenceMechanism;

import java.util.concurrent.Callable;

public class ChildPathsActivator implements Callable<ChildPaths> {
    private final Records records;
    private final PersistenceMechanism persistenceMechanism;

    public ChildPathsActivator(Records records, PersistenceMechanism persistenceMechanism) {
        this.records = records;
        this.persistenceMechanism = persistenceMechanism;
    }

    @Override
    public ChildPaths call() throws Exception {
        // TODO Make work with HSQL
        return persistenceMechanism.equals(PersistenceMechanism.ORACLE) ? new ChildPathsFromSql((SqlRecords) records) : new ChildPathsFromRecords(records);
    }
}
