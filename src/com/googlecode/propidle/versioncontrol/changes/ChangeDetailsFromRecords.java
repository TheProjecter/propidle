package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.lazyrecords.Definition;
import com.googlecode.lazyrecords.Keyword;
import com.googlecode.lazyrecords.Record;
import com.googlecode.lazyrecords.Records;
import com.googlecode.propidle.util.time.Clock;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Callable1;

import java.util.UUID;

import static com.googlecode.lazyrecords.Definition.constructors.definition;
import static com.googlecode.lazyrecords.Keywords.keyword;
import static com.googlecode.lazyrecords.Record.constructors.record;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;

public class ChangeDetailsFromRecords {

    public static final Keyword<Integer> REVISION_NUMBER = keyword("revision_number", Integer.class);
    public static final Keyword<UUID> ID = keyword("id", UUID.class);
    public static final Keyword<String> NAME = keyword("name", String.class);
    public static final Keyword<String> VALUE = keyword("value", String.class);
    public static final Definition CHANGE_DETAILS = definition("change_details", REVISION_NUMBER, NAME, VALUE);

    private final Records records;
    private final Clock clock;

    public ChangeDetailsFromRecords(Records records, Clock clock) {
        this.records = records;
        this.clock = clock;
    }

    public void createDetails(RevisionNumber revisionNumber) {
        records.add(CHANGE_DETAILS, record().set(ID, UUID.randomUUID()).set(REVISION_NUMBER, revisionNumber.value()).
                set(NAME, "date").set(VALUE, clock.time().toString()));
    }

    public Iterable<ChangeDetail> changesForRevision(RevisionNumber revisionNumber) {
        return records.get(CHANGE_DETAILS).filter(where(REVISION_NUMBER, is(revisionNumber.value()))).map(toChangeDetail());
    }

    private Callable1<? super Record, ? extends ChangeDetail> toChangeDetail() {
        return new Callable1<Record, ChangeDetail>() {
            @Override
            public ChangeDetail call(Record record) throws Exception {
                return new ChangeDetail(record.get(NAME), record.get(VALUE));
            }
        };
    }
}
