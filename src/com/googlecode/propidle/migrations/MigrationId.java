package com.googlecode.propidle.migrations;

import static java.lang.String.format;

public class MigrationId {
    private final MigrationNumber number;
    private final MigrationName name;

    public static MigrationId migrationId(MigrationNumber number, MigrationName name) {
        return new MigrationId(number, name);
    }

    protected MigrationId(MigrationNumber number, MigrationName name) {
        this.number = number;
        this.name = name;
    }

    public MigrationNumber number() {
        return number;
    }

    public MigrationName name() {
        return name;
    }

    @Override
    public String toString() {
        return format("%s-%s", number(), name());
    }
}
