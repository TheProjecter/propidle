package com.googlecode.propidle.migrations.sql;

import java.sql.SQLException;
import java.sql.Connection;

public class MigrationUserSqlExecutor implements SqlExecutor {
    private final Connection connection;

    public MigrationUserSqlExecutor(Connection connection) {
        this.connection = connection;
    }

    public void execute(String sql) throws SQLException {
        connection.createStatement().execute(sql);
    }
}
