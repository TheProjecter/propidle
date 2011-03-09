package com.googlecode.propidle.persistence.jdbc;

public class MigrationConnectionDetails extends ConnectionDetails {
    protected MigrationConnectionDetails(String url, String user, String password) {
        super(url, user, password);
    }

    public static MigrationConnectionDetails migrationConnectionDetails(String url, String user, String password) {
        return new MigrationConnectionDetails(url, user, password);
    }
}
