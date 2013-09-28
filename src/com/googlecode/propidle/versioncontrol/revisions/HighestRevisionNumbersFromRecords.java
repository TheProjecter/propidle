package com.googlecode.propidle.versioncontrol.revisions;

import com.googlecode.lazyrecords.Definition;
import com.googlecode.lazyrecords.Keyword;
import com.googlecode.lazyrecords.Record;
import com.googlecode.lazyrecords.Records;

import static com.googlecode.lazyrecords.Definition.constructors.definition;
import static com.googlecode.lazyrecords.Keyword.constructors.keyword;
import static com.googlecode.lazyrecords.Record.constructors.record;
import static com.googlecode.propidle.versioncontrol.revisions.HighestExistingRevisionNumber.highestExistingRevisionNumber;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;

public class HighestRevisionNumbersFromRecords implements HighestRevisionNumbers {

    public static final Keyword<Integer> REVISION_NUMBER = keyword("revision_number", Integer.class);
    public static final Definition HIGHEST_REVISION = definition("highest_revision", REVISION_NUMBER);

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

}
