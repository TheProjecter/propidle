package com.googlecode.propidle.migrations.sql;

import com.googlecode.propidle.util.tinytype.StringTinyType;
import com.googlecode.propidle.util.tinytype.StringTinyType;

public class SqlFile extends StringTinyType<SqlFile> {
    private SqlFile(String value) {
        super(value);
    }

    public static SqlFile sqlFile(String value) {
        return new SqlFile(value);
    }
}
