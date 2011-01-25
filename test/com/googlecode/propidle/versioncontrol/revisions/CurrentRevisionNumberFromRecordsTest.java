package com.googlecode.propidle.versioncontrol.revisions;

import com.googlecode.totallylazy.records.Records;
import org.junit.Test;

import static com.googlecode.propidle.util.TemporaryRecords.temporaryRecords;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.propidle.versioncontrol.revisions.CurrentRevisionNumberFromRecords.defineHighestRevisionRecord;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CurrentRevisionNumberFromRecordsTest {
    private final Records records = defineHighestRevisionRecord(temporaryRecords());

    @Test
    public void shouldIncrementTheLastRevisionNumberAndThenHoldOnToIt() {
        CurrentRevisionNumberFromRecords revisionNumbers = new CurrentRevisionNumberFromRecords(records);
        assertThat(revisionNumbers.current(), is(revisionNumber(0)));
        assertThat(revisionNumbers.current(), is(revisionNumber(0)));
    }
}
