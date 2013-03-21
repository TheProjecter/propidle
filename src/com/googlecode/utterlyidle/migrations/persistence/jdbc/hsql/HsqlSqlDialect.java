package com.googlecode.utterlyidle.migrations.persistence.jdbc.hsql;

import com.googlecode.utterlyidle.migrations.persistence.jdbc.BySemiColonLineExtractor;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.SqlDialect;
import com.googlecode.utterlyidle.migrations.sql.SqlFile;
import com.googlecode.utterlyidle.migrations.sql.SqlLine;

public class HsqlSqlDialect implements SqlDialect {
    public static final String HSQL = "hsql";
    private final BySemiColonLineExtractor bySemiColonLineExtractor = new BySemiColonLineExtractor();

    @Override
    public String toString() {
        return HSQL;
    }

    public Iterable<SqlLine> linesIn(SqlFile sqlFile) {
        return bySemiColonLineExtractor.linesIn(sqlFile);
    }

    public String name() {
        return HSQL;
    }
}
