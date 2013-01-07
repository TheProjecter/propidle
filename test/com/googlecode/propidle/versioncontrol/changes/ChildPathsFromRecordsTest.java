package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.lazyrecords.memory.MemoryRecords;
import junit.framework.TestCase;
import org.junit.Test;

import static com.googlecode.lazyrecords.Record.constructors.record;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;

public class ChildPathsFromRecordsTest {
    @Test
    public void shouldNotReturnEmptyProperties() {
        verify("/empty");
        verify("/dir/empty");
        verify("/parent/dir/empty");
    }

    private void verify(String file) {
        int revisionNumber = 0;
        MemoryRecords records = new MemoryRecords();
        ChildPathsFromRecords childPathsFromRecords = new ChildPathsFromRecords(records);
        String ignoreMe = "/ignore/me";
        records.add(CHANGES, record(PROPERTIES_PATH, ignoreMe, PROPERTY_NAME, "baz", UPDATED_VALUE, "bin", REVISION_NUMBER, revisionNumber++));
        records.add(CHANGES, record(PROPERTIES_PATH, file, PROPERTY_NAME, "foo", UPDATED_VALUE, "bar", REVISION_NUMBER, revisionNumber++));
        assertThat(childPathsFromRecords.childPaths(propertiesPath("/")), containsInAnyOrder(propertiesPath(file), propertiesPath(ignoreMe)));
        records.add(CHANGES, record(PROPERTIES_PATH, file, PROPERTY_NAME, "foo", PREVIOUS_VALUE, "bar", UPDATED_VALUE, null, REVISION_NUMBER, revisionNumber++));
        assertThat(childPathsFromRecords.childPaths(propertiesPath("/")), not(contains(propertiesPath(file))));
        assertThat(childPathsFromRecords.childPaths(propertiesPath("/")), contains(propertiesPath(ignoreMe)));
    }
}
