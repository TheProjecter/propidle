package com.googlecode.utterlyidle.migrations.persistence.jdbc;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.migrations.sql.SqlFile;
import com.googlecode.utterlyidle.migrations.sql.SqlLine;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.migrations.sql.SqlFile.sqlFile;
import static com.googlecode.utterlyidle.migrations.sql.SqlLine.sqlLine;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BySemiColonLineExtractorTest {

    @Test
    public void canExtractFromFileContainingOneLine() {
        SqlFile file = aSqlFileContaining(createTable("mahogany"));
        BySemiColonLineExtractor extractor = new BySemiColonLineExtractor();
        Sequence<SqlLine> lines = sequence(extractor.linesIn(file));
        assertThat(lines.size(), is(equalTo(1)));
        assertThat(lines.first(), is(equalTo(createTable("mahogany"))));
    }

    @Test
    public void canHandleFinalSemiColonAndNonTrimmedLines() {
        SqlFile file = sqlFile("GRANT select, insert, update, delete on changes to PROPIDLE_USER; ");
        BySemiColonLineExtractor extractor = new BySemiColonLineExtractor();
        Sequence<SqlLine> lines = sequence(extractor.linesIn(file));
        assertThat(lines.size(), is(equalTo(1)));
        assertThat(lines.first(), is(equalTo(sqlLine("GRANT select, insert, update, delete on changes to PROPIDLE_USER"))));
    }

    @Test
    public void canExtractFromFileContainingManyLines() {
        SqlFile file = aSqlFileContaining(createTable("mahogany"), createTable("plastic"), createTable("coffee"));
        BySemiColonLineExtractor extractor = new BySemiColonLineExtractor();
        Sequence<SqlLine> lines = sequence(extractor.linesIn(file));
        assertThat(lines.size(), is(equalTo(3)));
        assertThat(lines.first(), is(equalTo(createTable("mahogany"))));
        assertThat(lines.second(), is(equalTo(createTable("plastic"))));
        assertThat(lines.last(), is(equalTo(createTable("coffee"))));
    }

    private SqlFile aSqlFileContaining(SqlLine ...lines) {
        return sqlFile(sequence(lines).map(asString()).toString(";\n"));
    }

    private SqlLine createTable(String value) {
        return sqlLine("create table " + value + " (\n" +
                "    id        INTEGER          NOT NULL,\n" +
                "    CONSTRAINT first_" + value + "_pk PRIMARY KEY(id)\n" +
                ")");
    }
}
