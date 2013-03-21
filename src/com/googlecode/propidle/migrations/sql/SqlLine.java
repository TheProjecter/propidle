package com.googlecode.propidle.migrations.sql;

import com.googlecode.propidle.util.tinytype.StringTinyType;
import com.googlecode.propidle.util.tinytype.StringTinyType;

public class SqlLine extends StringTinyType<SqlLine> {
    private SqlLine(String value) {
        super(value);
    }

    public static SqlLine sqlLine(String value) {
        return new SqlLine(value);
    }
}
