package com.googlecode.utterlyidle.migrations.sql;

public interface SqlLineExtractor {
    Iterable<SqlLine> extractLinesFrom(SqlFile sqlFile);
}
