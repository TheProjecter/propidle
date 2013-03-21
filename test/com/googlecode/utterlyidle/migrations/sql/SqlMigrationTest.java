package com.googlecode.utterlyidle.migrations.sql;

import com.googlecode.utterlyidle.migrations.MigrationId;
import org.junit.Test;

import java.net.URL;

import static com.googlecode.utterlyidle.migrations.MigrationName.migrationName;
import static com.googlecode.utterlyidle.migrations.MigrationNumber.migrationNumber;
import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class SqlMigrationTest {
    @Test
    public void canParseMigrationIdFromSqlFileName() throws Exception {
        MigrationId id = SqlMigration.parseMigrationFileId(new URL("file:///somewhere/whatever.dot/001-some_name.sql"));

        assertThat(id.name(), is(migrationName("some_name")));
        assertThat(id.number(), is(migrationNumber(1)));
    }

    @Test
    public void willNotBeAbleToParseMigrationIdFromInvalidUrls() throws Exception {
        URL url = new URL("file:///somewhere/whatever.dot/001_some_name.sql");
        try {
            SqlMigration.parseMigrationFileId(url);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString(url.toString()));
        }
    }
}
