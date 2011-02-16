package com.googlecode.propidle.migrations;

import com.googlecode.propidle.util.tinytype.StringTinyType;

import java.net.URL;

public class MigrationName extends StringTinyType<MigrationName>{
    public static MigrationName migrationName(String value) {
        return new MigrationName(value);
    }

    protected MigrationName(String value) {
        super(value);
    }
}
