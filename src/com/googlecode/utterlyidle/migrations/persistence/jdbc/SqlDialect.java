package com.googlecode.utterlyidle.migrations.persistence.jdbc;

import com.googlecode.utterlyidle.migrations.sql.SqlFile;
import com.googlecode.utterlyidle.migrations.sql.SqlLine;

public interface SqlDialect {
    Iterable<SqlLine> linesIn(SqlFile sqlFile);
    String name();
}
