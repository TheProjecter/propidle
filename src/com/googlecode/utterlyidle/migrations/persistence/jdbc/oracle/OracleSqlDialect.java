package com.googlecode.utterlyidle.migrations.persistence.jdbc.oracle;

import com.googlecode.utterlyidle.migrations.persistence.jdbc.BySemiColonLineExtractor;
import com.googlecode.utterlyidle.migrations.persistence.jdbc.SqlDialect;
import com.googlecode.utterlyidle.migrations.sql.SqlFile;
import com.googlecode.utterlyidle.migrations.sql.SqlLine;

public class OracleSqlDialect implements SqlDialect{
    public static final String ORACLE = "oracle";
    private final BySemiColonLineExtractor bySemiColonLineExtractor = new BySemiColonLineExtractor();

    @Override
    public String toString() {
        return ORACLE;
    }

    public Iterable<SqlLine> linesIn(SqlFile sqlFile) {
        return bySemiColonLineExtractor.linesIn(sqlFile);
    }

    public String name() {
        return ORACLE;
    }


}
