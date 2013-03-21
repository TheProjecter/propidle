package com.googlecode.utterlyidle.migrations.persistence.jdbc.hsql;

import com.googlecode.lazyrecords.Definition;
import com.googlecode.lazyrecords.Keyword;
import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.lazyrecords.sql.SqlSchema;
import com.googlecode.lazyrecords.sql.grammars.AnsiSqlGrammar;
import com.googlecode.utterlyidle.migrations.util.DriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import static com.googlecode.lazyrecords.Definition.constructors.definition;
import static com.googlecode.lazyrecords.Keywords.keyword;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HsqlRecordLockTest {
    private static final Keyword<String> SOME_FIELD = keyword("SOME_FIELD", String.class);
    private static final Definition TEST_TABLE = definition("TEST_TABLE", SOME_FIELD);
    private Connection firstConnection;
    private Connection secondConnection;

    @Before
    public void defineRecords() throws Exception {
        Connection connection = connection();
        SqlRecords sqlRecords = new SqlRecords(connection);
        SqlSchema sqlSchema = new SqlSchema(sqlRecords, new AnsiSqlGrammar());
        sqlSchema.define(TEST_TABLE);
        connection().commit();
        connection().close();
    }

    @Before
    public void openConnections() throws Exception {
        firstConnection = connection();
        secondConnection = connection();
    }

    @After
    public void closeConnections() throws Exception {
        close(firstConnection);
        close(secondConnection);
    }

    @Test
    public void shouldStayLockedUntilReleased() throws SQLException, InterruptedException {
        int millisToWaitForLock = 200;
        assertThat("FIRST connection should be able to get lock",
                   canGetLock(firstConnection, millisToWaitForLock),
                   is(true));

        assertThat("SECOND connection should NOT be able to get lock until FIRST connection is closed",
                   canGetLock(secondConnection, 200),
                   is(false));

        firstConnection.close();

        assertThat("SECOND connection should be able to get lock after FIRST connection is closed",
                   canGetLock(secondConnection, 200),
                   is(true));
    }

    private boolean canGetLock(final Connection connection, int millisToWaitForLock) throws InterruptedException {
        final CountDownLatch managedToGetALock = new CountDownLatch(1);
        Thread task = new Thread(new Runnable() {
            public void run() {
                new HsqlRecordLock(connection).aquire(TEST_TABLE);
                managedToGetALock.countDown();
            }
        });
        task.start();
        task.join(millisToWaitForLock);
        return managedToGetALock.getCount() == 0;
    }

    private void close(Connection firstConnection) throws SQLException {
        if (!firstConnection.isClosed()) firstConnection.close();
    }

    private Connection connection() throws Exception {
        return DriverManager.getConnection("jdbc:hsqldb:mem:HsqlRecordLockTest", "SA", "");
    }
}
