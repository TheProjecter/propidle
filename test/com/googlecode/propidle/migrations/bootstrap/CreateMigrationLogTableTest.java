package com.googlecode.propidle.migrations.bootstrap;

import static com.googlecode.propidle.migrations.MigrationName.migrationName;
import static com.googlecode.propidle.migrations.MigrationNumber.migrationNumber;
import com.googlecode.propidle.migrations.bootstrap.hsql.HsqlCreateMigrationLogTable;
import com.googlecode.propidle.migrations.bootstrap.oracle.OracleCreateMigrationLogTable;
import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.propidle.migrations.log.MigrationLogFromRecords;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import com.googlecode.propidle.persistence.jdbc.ConnectionDetails;
import static com.googlecode.propidle.persistence.jdbc.ConnectionDetails.connectionDetails;
import static com.googlecode.totallylazy.Option.option;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.totallylazy.records.sql.SqlRecords;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.Date;

public class CreateMigrationLogTableTest {
    private Connection connection;
    private SqlRecords records;
    private MigrationLog migrationLog;

    @Before
    public void createRecords() throws Exception {
        ConnectionDetails connectionDetails = connectionDetails("jdbc:hsqldb:mem:HsqlCreateMigrationLogTableTest", "SA", "");
//        ConnectionDetails connectionDetails = connectionDetails("jdbc:oracle:thin:@//90.207.234.87:1521/XENONCT1", "PROPIDLE_OWNER", "PROPIDLE_OWNER");
        connection = connectionDetails.openConnection();
        records = new SqlRecords(connection);
        migrationLog = new MigrationLogFromRecords(records);
    }

    @After
    public void closeConnection() throws Exception {
        if (connection != null) connection.close();
    }

    @Test
    public void willBootstrapMigrationLogTable() throws Exception {
//        Runnable bootstrapper = new OracleCreateMigrationLogTable(records);
        Runnable bootstrapper = new HsqlCreateMigrationLogTable(records);

        bootstrapper.run();
        bootstrapper.run(); // Should not fail

        Sequence<MigrationLogItem> migrations = sequence(
                new MigrationLogItem(new Date(), migrationNumber(1), migrationName("create_toddlers_table")),
                new MigrationLogItem(new Date(), migrationNumber(2), migrationName("index_dribble_column")));

        migrationLog.add(migrations);

        assertThat(migrationLog.get(migrations.first().number()), is(option(migrations.first())));
        assertThat(migrationLog.get(migrations.second().number()), is(option(migrations.second())));
    }
}
