package com.googlecode.propidle.persistence.jdbc.hsql;

import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.sql.SqlRecords;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import static com.googlecode.totallylazy.records.Keyword.keyword;
import com.googlecode.propidle.DriverManager;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HsqlRecordLockTest {
    private static final Keyword TEST_TABLE = keyword("TEST_TABLE");
    private static final Keyword<String> SOME_FIELD = keyword("SOME_FIELD", String.class);
    private Connection firstConnection;
    private Connection secondConnection;

    @Before
    public void defineRecords() throws Exception {
        Connection connection = connection();
        new SqlRecords(connection).define(TEST_TABLE, SOME_FIELD);
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
        tryTestWithWaitTimeOf(200);
    }

    private void tryTestWithWaitTimeOf(final int millisToWaitForLock) throws InterruptedException, SQLException {
        assertThat("FIRST connection should be able to get lock",
                   canGetLock(firstConnection, millisToWaitForLock),
                   is(true));

        assertThat("SECOND connection should NOT be able to get lock until FIRST connection is closed",
                   canGetLock(secondConnection, millisToWaitForLock),
                   is(false));

        firstConnection.close();

        assertThat("SECOND connection should be able to get lock after FIRST connection is closed",
                   canGetLock(secondConnection, millisToWaitForLock),
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
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:HsqlRecordLockTest", "SA", "");
        return connection;
    }
}
