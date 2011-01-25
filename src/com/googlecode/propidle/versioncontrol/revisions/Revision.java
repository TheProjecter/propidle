package com.googlecode.propidle.versioncontrol.revisions;

public class Revision {
    private final RevisionNumber number;

    public static Revision revision(RevisionNumber number) {
        return new Revision(number);
    }
    protected Revision(RevisionNumber number) {
        this.number = number;
    }

    public RevisionNumber number() {
        return number;
    }
}
