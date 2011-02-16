package com.googlecode.propidle.migrations.sql;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.containsString;

import java.net.URL;

import static com.googlecode.propidle.migrations.MigrationName.migrationName;
import static com.googlecode.propidle.migrations.MigrationNumber.migrationNumber;
import com.googlecode.propidle.migrations.MigrationId;
import static junit.framework.Assert.fail;

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
