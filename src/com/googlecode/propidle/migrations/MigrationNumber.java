package com.googlecode.propidle.migrations;

import com.googlecode.propidle.util.tinytype.IntegerTinyType;
import static com.googlecode.totallylazy.Sequences.sequence;

import java.net.URL;
import static java.lang.Integer.parseInt;

public class MigrationNumber extends IntegerTinyType<MigrationNumber> {
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
