package acceptance.steps.givens;

import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.lazyrecords.Records;
import com.googlecode.totallylazy.Block;
import com.googlecode.utterlyidle.Application;
import com.googlecode.yadic.Container;

import java.sql.Connection;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords.HIGHEST_REVISION;
import static com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbersFromRecords.serialiseRevisionNumber;

public class CurrentRevision implements Callable<RevisionNumber> {
   private final Application application;

    private RevisionNumber revisionNumber;

    public CurrentRevision(Application application) {
        this.application = application;
    }

    public RevisionNumber call() throws Exception {
        application.usingRequestScope(new Block<Container>() {
            @Override
            protected void execute(Container container) throws Exception {
                final Records records = container.get(Records.class);
                final Connection connection = container.get(Connection.class);
                records.remove(HIGHEST_REVISION);
                records.add(HIGHEST_REVISION, serialiseRevisionNumber(revisionNumber.minus(1)));
                connection.commit();
            }
        });
        return revisionNumber;
    }

    public CurrentRevision startsAt(RevisionNumber revisionNumber) {
        this.revisionNumber = revisionNumber;
        return this;
    }
}