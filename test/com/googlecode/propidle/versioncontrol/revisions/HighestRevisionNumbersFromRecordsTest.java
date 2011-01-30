package com.googlecode.propidle.versioncontrol.revisions;

import com.googlecode.totallylazy.records.Records;
import org.junit.Test;

import static com.googlecode.propidle.util.TemporaryRecords.temporaryRecords;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords.defineHighestRevisionRecord;
import static com.googlecode.propidle.versioncontrol.revisions.NewRevisionNumber.newRevisionNumber;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HighestRevisionNumbersFromRecordsTest {
    private final Records records = defineHighestRevisionRecord(temporaryRecords());

    @Test
    public void shouldIncrementTheLastRevisionNumberAndThenHoldOnToIt() {
        HighestRevisionNumbersFromRecords revisionNumbers = new HighestRevisionNumbersFromRecords(records);
        assertThat(revisionNumbers.newRevisionNumber(), is(newRevisionNumber(0)));
        assertThat(revisionNumbers.newRevisionNumber(), is(newRevisionNumber(0)));
    }
}
