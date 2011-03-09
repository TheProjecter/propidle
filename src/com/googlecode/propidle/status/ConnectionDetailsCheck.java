package com.googlecode.propidle.status;

import com.googlecode.propidle.persistence.jdbc.ConnectionDetails;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;

import java.sql.Connection;

import static com.googlecode.propidle.status.StatusCheckName.statusCheckName;
import static com.googlecode.propidle.status.StatusCheckResult.statusCheckResult;
import static com.googlecode.totallylazy.Closeables.reflectiveClose;
import static com.googlecode.totallylazy.Exceptions.handleException;
import static com.googlecode.totallylazy.Predicates.instanceOf;


public class ConnectionDetailsCheck implements StatusCheck {
    private final ConnectionDetails connectionDetails;

    public ConnectionDetailsCheck(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public StatusCheckResult check() throws Exception {
        StatusCheckResult result = statusCheckResult(
                statusCheckName(getClass().getSimpleName())).
                add("JDBC URL", connectionDetails.url()).
                add("Username", connectionDetails.user()).
                add("Result", (canConnect() ? "PASS" : "FAIL"));

        return result;
    }

    private boolean canConnect() throws Exception {
        Option<Connection> connection = handleException(openConnection(), instanceOf(Exception.class)).call(connectionDetails);
        if (connection.isEmpty()) {
            return false;
        }
        connection.get().close();
        return true;
    }

    private Callable1<ConnectionDetails, Connection> openConnection() {
        return new Callable1<ConnectionDetails, Connection>() {
            public Connection call(ConnectionDetails connectionDetails) throws Exception {
                return connectionDetails.openConnection();
            }
        };
    }
}