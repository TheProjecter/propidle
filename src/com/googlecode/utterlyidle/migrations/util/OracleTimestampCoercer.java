package com.googlecode.utterlyidle.migrations.util;

import oracle.sql.TIMESTAMP;

import java.sql.SQLException;
import java.sql.Timestamp;

public class OracleTimestampCoercer {
    public static Timestamp toTimeStamp(Object o) {
        try {
            return ((TIMESTAMP) o).timestampValue();
        } catch (SQLException e) {
            throw new RuntimeException("Stop using Oracle. Just stop.", e);
        }
    }
}
