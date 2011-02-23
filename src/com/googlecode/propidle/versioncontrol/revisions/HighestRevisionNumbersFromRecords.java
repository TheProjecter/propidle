package com.googlecode.propidle.versioncontrol.revisions;

import static com.googlecode.propidle.versioncontrol.revisions.HighestExistingRevisionNumber.highestExistingRevisionNumber;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import com.googlecode.totallylazy.records.Keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;

public class HighestRevisionNumbersFromRecords implements HighestRevisionNumbers {
    public static final Keyword<String> HIGHEST_REVISION = Keyword.keyword("highest_revision", String.class);
    public static final Keyword<Number> REVISION_NUMBER = Keyword.keyword("revision_number", Number.class);

    private final Records records;
    private NewRevisionNumber newRevisionNumber;
    private HighestExistingRevisionNumber highestExistingRevisionNumber;

    public HighestRevisionNumbersFromRecords(Records records) {
        this.records = records;
    }

    public NewRevisionNumber newRevisionNumber() {
        if (newRevisionNumber == null) {
            newRevisionNumber = NewRevisionNumber.newRevisionNumber(deserialiseHighestExistingRevisionNumber().plus(1));
            records.remove(HIGHEST_REVISION);
            records.add(HIGHEST_REVISION, serialiseRevisionNumber(newRevisionNumber));
        }
        return newRevisionNumber;
    }

    public HighestExistingRevisionNumber highestExistingRevision() {
        if (highestExistingRevisionNumber == null) {
            highestExistingRevisionNumber = deserialiseHighestExistingRevisionNumber();
        }
        return highestExistingRevisionNumber;
    }

    private HighestExistingRevisionNumber deserialiseHighestExistingRevisionNumber() {
        return deserialiseRevisionNumber(
                    records.get(HIGHEST_REVISION).headOption().
                            getOrElse(serialiseRevisionNumber(revisionNumber(-1))));
    }

    public static Record serialiseRevisionNumber(RevisionNumber revisionNumber) {
        return record().set(REVISION_NUMBER, revisionNumber.value());
    }

    private HighestExistingRevisionNumber deserialiseRevisionNumber(Record record) {
        return highestExistingRevisionNumber(record.get(REVISION_NUMBER));
    }

    public static Records defineHighestRevisionRecord(Records records) {
        records.define(HIGHEST_REVISION, REVISION_NUMBER);
        return records;
    }
}
