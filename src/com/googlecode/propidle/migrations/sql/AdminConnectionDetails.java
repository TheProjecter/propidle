package com.googlecode.propidle.migrations.sql;

import com.googlecode.propidle.persistence.jdbc.ConnectionDetails;

public class AdminConnectionDetails extends ConnectionDetails {
    public static AdminConnectionDetails adminConnectionDetails(ConnectionDetails connectionDetails) {
        return adminConnectionDetails(connectionDetails.url(), connectionDetails.user(), connectionDetails.password());
    }

    public static AdminConnectionDetails adminConnectionDetails(String url, String user, String password) {
        return new AdminConnectionDetails(url, user, password);
    }

    protected AdminConnectionDetails(String url, String user, String password) {
        super(url, user, password);
    }
}
