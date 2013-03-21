package com.googlecode.propidle.migrations.sql;

public interface SqlLineExtractor {
    Iterable<SqlLine> extractLinesFrom(SqlFile sqlFile);
}
