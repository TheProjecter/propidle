package com.googlecode.propidle.persistence.jdbc;

import com.googlecode.propidle.persistence.PersistenceException;
import com.googlecode.propidle.persistence.Transaction;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlPersistence implements Transaction{
    private final Connection connection;

    public SqlPersistence(Connection connection) {
        this.connection = connection;
        try {
            // Start transaction
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new PersistenceException("Could not begin transaction", e);
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new PersistenceException("Could not commit transaction", e);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new PersistenceException("Could not rollback transaction", e);
        }
    }
}
