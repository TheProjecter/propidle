package com.googlecode.propidle.versioncontrol.revisions;

import com.googlecode.propidle.util.IntegerTinyType;

public class RevisionNumber extends IntegerTinyType<RevisionNumber> {
    public static RevisionNumber revisionNumber(String value) {
        return new RevisionNumber(value);
    }

    public static RevisionNumber revisionNumber(int value) {
        return new RevisionNumber(value);
    }

    protected RevisionNumber(String value) {
        super(Integer.valueOf(value));
    }

    protected RevisionNumber(Integer value) {
        super(value);
    }

    public RevisionNumber plus(int value) {
        return revisionNumber(this.value() + value);
    }

    public RevisionNumber minus(int value) {
        return plus(-value);
    }
}
