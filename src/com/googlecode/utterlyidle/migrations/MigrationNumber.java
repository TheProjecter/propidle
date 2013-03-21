package com.googlecode.utterlyidle.migrations;

import com.googlecode.utterlyidle.migrations.util.tinytype.IntegerTinyType;

public class MigrationNumber extends IntegerTinyType<MigrationNumber> {
    public static MigrationNumber migrationNumber(Number value) {
        return migrationNumber(value.intValue());
    }

    public static MigrationNumber migrationNumber(Integer value) {
        return new MigrationNumber(value);
    }

    protected MigrationNumber(Integer value) {
        super(value);
    }

    @Override
    public String toString() {
        return String.format("%04d", value());
    }
}
