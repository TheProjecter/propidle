package com.googlecode.propidle.persistence.jdbc;

public interface ConnectionProvider {
    public java.sql.Connection getConnection();
}
