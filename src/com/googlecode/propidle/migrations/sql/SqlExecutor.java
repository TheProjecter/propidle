package com.googlecode.propidle.migrations.sql;

import java.sql.SQLException;

public interface SqlExecutor {
    void execute(String sql) throws SQLException;
}
