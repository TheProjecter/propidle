package com.googlecode.utterlyidle.migrations.sql;

import com.googlecode.utterlyidle.migrations.util.tinytype.StringTinyType;

public class SqlLine extends StringTinyType<SqlLine>{
    private SqlLine(String value) {
        super(value);
    }

    public static SqlLine sqlLine(String value) {
        return new SqlLine(value);
    }
}
