package com.googlecode.propidle.migrations.persistence.jdbc;

import com.googlecode.propidle.migrations.sql.SqlFile;
import com.googlecode.propidle.migrations.sql.SqlLine;

public interface SqlDialect {
    Iterable<SqlLine> linesIn(SqlFile sqlFile);
    String name();
}
