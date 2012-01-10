package com.googlecode.propidle.versioncontrol.revisions;

import static com.googlecode.propidle.util.TestRecords.testRecordsWithAllMigrationsRun;
import static com.googlecode.propidle.versioncontrol.revisions.NewRevisionNumber.newRevisionNumber;
import com.googlecode.lazyrecords.Records;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

public class HighestRevisionNumbersFromRecordsTest {
    private final Records records = testRecordsWithAllMigrationsRun();
    private final HighestRevisionNumbersFromRecords revisionNumbers = new HighestRevisionNumbersFromRecords(records);

    @Test
    public void shouldIncrementTheLastRevisionNumberAndThenHoldOnToIt() {
        assertThat(revisionNumbers.newRevisionNumber(), is(newRevisionNumber(0)));
        assertThat(revisionNumbers.newRevisionNumber(), is(newRevisionNumber(0)));
    }
}
