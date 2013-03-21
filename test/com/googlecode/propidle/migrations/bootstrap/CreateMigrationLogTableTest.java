package com.googlecode.propidle.migrations.bootstrap;

import com.googlecode.lazyrecords.sql.SqlRecords;
import com.googlecode.lazyrecords.sql.SqlSchema;
import com.googlecode.lazyrecords.sql.grammars.AnsiSqlGrammar;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.propidle.migrations.ModuleName;
import com.googlecode.propidle.migrations.bootstrap.hsql.HsqlCreateMigrationLogTable;
import com.googlecode.propidle.migrations.log.MigrationLog;
import com.googlecode.propidle.migrations.log.MigrationLogFromRecords;
import com.googlecode.propidle.migrations.log.MigrationLogItem;
import com.googlecode.propidle.migrations.persistence.jdbc.ConnectionDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.Date;

import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.propidle.migrations.MigrationName.migrationName;
import static com.googlecode.propidle.migrations.MigrationNumber.migrationNumber;
import static com.googlecode.propidle.migrations.ModuleName.moduleName;
import static com.googlecode.propidle.migrations.persistence.jdbc.ConnectionDetails.connectionDetails;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CreateMigrationLogTableTest {
    private Connection connection;
    private SqlRecords records;
    private MigrationLog migrationLog;
    private SqlSchema schema;

    @Before
    public void createRecords() throws Exception {
        ConnectionDetails connectionDetails = connectionDetails("jdbc:hsqldb:mem:HsqlCreateMigrationLogTableTest", "SA", "");
//        ConnectionDetails connectionDetails = connectionDetails("jdbc:oracle:thin:@//90.207.234.87:1521/XENONCT1", "PROPIDLE_OWNER", "PROPIDLE_OWNER");
        connection = connectionDetails.openConnection();
        records = new SqlRecords(connection);
        schema = new SqlSchema(records, new AnsiSqlGrammar());
        migrationLog = new MigrationLogFromRecords(records);
    }

    @After
    public void closeConnection() throws Exception {
        if (connection != null) connection.close();
    }

    @Test
    public void willBootstrapMigrationLogTable() throws Exception {
//        Runnable bootstrapper = new OracleCreateMigrationLogTable(records, schema);
        Runnable bootstrapper = new HsqlCreateMigrationLogTable(records, schema);

        bootstrapper.run();
        bootstrapper.run(); // Should not fail

        ModuleName moduleName = moduleName(getClass().getSimpleName());
        Sequence<MigrationLogItem> migrations = sequence(
                new MigrationLogItem(new Date(), migrationNumber(1), migrationName("create_toddlers_table"), moduleName),
                new MigrationLogItem(new Date(), migrationNumber(2), migrationName("index_dribble_column"), moduleName));

        migrationLog.add(migrations);

        assertThat(migrationLog.get(migrations.first().number(), moduleName), is(option(migrations.first())));
        assertThat(migrationLog.get(migrations.second().number(), moduleName), is(option(migrations.second())));
    }
}
