package com.googlecode.propidle.versioncontrol.revisions;

import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;

import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.totallylazy.records.MapRecord.record;

public class CurrentRevisionNumberFromRecords implements CurrentRevisionNumber {
    public static final Keyword<String> HIGHEST_REVISION = Keyword.keyword("highest_revision", String.class);
    public static final Keyword<Integer> REVISION_NUMBER = Keyword.keyword("revision_number", Integer.class);

    private final Records records;
    private RevisionNumber current;

    public CurrentRevisionNumberFromRecords(Records records) {
        this.records = records;
    }

    public RevisionNumber current() {
        if (current == null) {
            current = currentHighestRevision().plus(1);
            records.remove(HIGHEST_REVISION);
            records.add(HIGHEST_REVISION, serialiseRevisionNumber(current));
        }
        return current;
    }

    private RevisionNumber currentHighestRevision() {
        return deserialiseRevisionNumber(
                records.get(HIGHEST_REVISION).headOption().
                        getOrElse(serialiseRevisionNumber(revisionNumber(-1))));
    }


    private Record serialiseRevisionNumber(RevisionNumber revisionNumber) {
        return record().set(REVISION_NUMBER, revisionNumber.value());
    }

    private RevisionNumber deserialiseRevisionNumber(Record record) {
        return revisionNumber(record.get(REVISION_NUMBER));
    }

    public static Records defineHighestRevisionRecord(Records records) {
        records.define(HIGHEST_REVISION, REVISION_NUMBER);
        return records;
    }
}
