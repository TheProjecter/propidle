package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.lazyrecords.Definition;
import com.googlecode.lazyrecords.Keyword;
import com.googlecode.lazyrecords.Record;
import com.googlecode.lazyrecords.Records;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;

import java.util.Map;
import java.util.UUID;

import static com.googlecode.lazyrecords.Definition.constructors.definition;
import static com.googlecode.lazyrecords.Keywords.keyword;
import static com.googlecode.lazyrecords.Record.constructors.record;
import static com.googlecode.totallylazy.Maps.entries;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;

public class ChangeDetailsFromRecords {

    public static final Keyword<Integer> REVISION_NUMBER = keyword("revision_number", Integer.class);
    public static final Keyword<UUID> ID = keyword("id", UUID.class);
    public static final Keyword<String> NAME = keyword("name", String.class);
    public static final Keyword<String> VALUE = keyword("value", String.class);
    public static final Definition CHANGE_DETAILS = definition("change_details", ID, REVISION_NUMBER, NAME, VALUE);

    private final Records records;
    private final ChangeDetails changeDetails;

    public ChangeDetailsFromRecords(Records records, ChangeDetails changeDetails) {
        this.records = records;
        this.changeDetails = changeDetails;
    }

    public void createDetails(RevisionNumber revisionNumber) {
        entries(changeDetails.value()).fold(records, changeDetailsFor(revisionNumber));
    }

    public ChangeDetails changesForRevision(RevisionNumber revisionNumber) {
        return new ChangeDetails(Maps.map(records.get(CHANGE_DETAILS).filter(where(REVISION_NUMBER, is(revisionNumber.value()))).map(toPair())));
    }

    private Callable1<? super Record, ? extends Pair<String, String>> toPair() {
        return new Callable1<Record, Pair<String, String>>() {
            @Override
            public Pair<String, String> call(Record record) throws Exception {
                return pair(record.get(NAME), record.get(VALUE));
            }
        };
    }

    private Callable2<Records, Map.Entry<String, String>, Records> changeDetailsFor(final RevisionNumber revisionNumber) {
        return new Callable2<Records, Map.Entry<String, String>, Records>() {
            @Override
            public Records call(Records records, Map.Entry<String, String> stringObjectEntry) throws Exception {
                records.add(CHANGE_DETAILS, record().set(ID, UUID.randomUUID()).set(REVISION_NUMBER, revisionNumber.value()).
                        set(NAME, stringObjectEntry.getKey()).set(VALUE, stringObjectEntry.getValue().toString()));
                return records;
            }
        };
    }
}
