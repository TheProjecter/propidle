package acceptance.steps.givens;

import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.lazyrecords.Records;

import java.util.concurrent.Callable;

import static com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords.HIGHEST_REVISION;
import static com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords.serialiseRevisionNumber;

public class CurrentRevision implements Callable<RevisionNumber> {
    private final Records records;

    private RevisionNumber revisionNumber;

    public CurrentRevision(Records records) {
        this.records = records;
    }

    public RevisionNumber call() throws Exception {
        records.remove(HIGHEST_REVISION);
        records.add(HIGHEST_REVISION, serialiseRevisionNumber(revisionNumber.minus(1)));
        return revisionNumber;
    }

    public CurrentRevision startsAt(RevisionNumber revisionNumber) {
        this.revisionNumber = revisionNumber;
        return this;
    }
}