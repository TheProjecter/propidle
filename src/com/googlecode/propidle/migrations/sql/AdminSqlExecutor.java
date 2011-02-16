package com.googlecode.propidle.migrations.sql;

import java.sql.SQLException;
import java.sql.Connection;

public class AdminSqlExecutor implements SqlExecutor {
    private final Connection connection;

    public AdminSqlExecutor(Connection connection) {
        this.connection = connection;
    }

    public void execute(String sql) throws SQLException {
        connection.createStatement().execute(sql);
    }
}
