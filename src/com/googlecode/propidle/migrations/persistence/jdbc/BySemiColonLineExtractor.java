package com.googlecode.propidle.migrations.persistence.jdbc;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.propidle.migrations.sql.SqlFile;
import com.googlecode.propidle.migrations.sql.SqlLine;

import static com.googlecode.totallylazy.Sequences.sequence;

public class BySemiColonLineExtractor {
    public Iterable<SqlLine> linesIn(SqlFile sqlFile) {
        return sequence(sqlFile.value().split(";\n")).map(sqlLine());
    }

    private Callable1<String, SqlLine> sqlLine() {
        return new Callable1<String, SqlLine>() {
            public SqlLine call(String s) throws Exception {
                return SqlLine.sqlLine(s.trim().replaceFirst(";$", ""));
            }
        };
    }
}
