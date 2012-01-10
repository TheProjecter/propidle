package com.googlecode.propidle.versioncontrol.revisions;

import static com.googlecode.propidle.versioncontrol.revisions.HighestExistingRevisionNumber.highestExistingRevisionNumber;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import com.googlecode.lazyrecords.Keyword;

import static com.googlecode.lazyrecords.Keywords.keyword;
import static com.googlecode.lazyrecords.MapRecord.record;

import com.googlecode.lazyrecords.Keywords;
import com.googlecode.lazyrecords.Record;
import com.googlecode.lazyrecords.Records;

public class HighestRevisionNumbersFromRecords implements HighestRevisionNumbers {
    public static final Keyword<String> HIGHEST_REVISION = keyword("highest_revision", String.class);
    public static final Keyword<Integer> REVISION_NUMBER = keyword("revision_number", Integer.class);

    private final Records records;
    private NewRevisionNumber newRevisionNumber;
    private HighestExistingRevisionNumber highestExistingRevisionNumber;

    public HighestRevisionNumbersFromRecords(Records records) {
        this.records = records;
        records.define(HIGHEST_REVISION, REVISION_NUMBER);
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

}
