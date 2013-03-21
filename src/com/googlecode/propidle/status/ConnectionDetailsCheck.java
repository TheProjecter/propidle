package com.googlecode.propidle.status;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.propidle.migrations.persistence.jdbc.ConnectionDetails;

import javax.sql.DataSource;
import java.sql.Connection;

import static com.googlecode.propidle.status.StatusCheckName.statusCheckName;
import static com.googlecode.propidle.status.StatusCheckResult.statusCheckResult;
import static com.googlecode.totallylazy.Exceptions.handleException;
import static com.googlecode.totallylazy.Predicates.instanceOf;


public class ConnectionDetailsCheck implements StatusCheck {
    private final ConnectionDetails connectionDetails;
    private final DataSource dataSource;

    public ConnectionDetailsCheck(ConnectionDetails connectionDetails, DataSource dataSource) {
        this.connectionDetails = connectionDetails;
        this.dataSource = dataSource;
    }

    public StatusCheckResult check() throws Exception {
        return statusCheckResult(
                statusCheckName(getClass().getSimpleName())).
                add("JDBC URL", connectionDetails.url()).
                add("Username", connectionDetails.user()).
                add("Result", (canConnect() ? "PASS" : "FAIL"));
    }

    private boolean canConnect() throws Exception {
        Option<Connection> connection = handleException(openConnection(), instanceOf(Exception.class)).call(dataSource);
        if (connection.isEmpty()) {
            return false;
        }
        connection.get().close();
        return true;
    }

    private Callable1<DataSource, Connection> openConnection() {
        return new Callable1<DataSource, Connection>() {
            public Connection call(DataSource dataSource) throws Exception {
                return dataSource.getConnection();
            }
        };
    }
}