package acceptance.steps.givens;

import acceptance.steps.Given;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.records.Records;

import java.util.concurrent.Callable;

import static acceptance.Values.with;
import static com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords.HIGHEST_REVISION;
import static com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords.serialiseRevisionNumber;

public class CurrentRevision implements Callable<RevisionNumber> {
    private final Records records;
    private final RevisionNumber revisionNumber;

    public CurrentRevision(Records records, RevisionNumber revisionNumber) {
        this.records = records;
        this.revisionNumber = revisionNumber;
    }

    public static Given<RevisionNumber> revisionNumbersStartAt(RevisionNumber revisionNumber){
        return new Given<RevisionNumber>(CurrentRevision.class, with(revisionNumber.minus(1)));
    }

    public RevisionNumber call() throws Exception {
        records.remove(HIGHEST_REVISION);
        records.add(HIGHEST_REVISION, serialiseRevisionNumber(revisionNumber));
        return revisionNumber;
    }
}