package com.googlecode.propidle.migrations.util;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.Sequence;

import java.sql.Connection;
import java.sql.Driver;

import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;

public class DriverManager {
    private static final Sequence<Pair<Predicate<String>, Runnable>> driverWakerUppers = sequence(
            Pair.<Predicate<String>, Runnable>pair(startsWith("jdbc:oracle"), registerDriver("oracle.jdbc.driver.OracleDriver")),
            Pair.<Predicate<String>, Runnable>pair(startsWith("jdbc:hsqldb"), registerDriver("org.hsqldb.jdbcDriver"))
    );

    public static Connection getConnection(String url, String username, String password) throws Exception {
        driverWakerUppers.filter(matches(url)).map(second(Runnable.class)).forEach(Runnables.run());
        return java.sql.DriverManager.getConnection(url, username, password);
    }

    private static Predicate<Pair<Predicate<String>, Runnable>> matches(final String url) {
        return new Predicate<Pair<Predicate<String>, Runnable>>() {
            public boolean matches(Pair<Predicate<String>, Runnable> pair) {
                return pair.first().matches(url);
            }
        };
    }

    private static Runnable registerDriver(final String name) {
        return new Runnable() {
            public void run() {
                try {
                    java.sql.DriverManager.registerDriver((Driver) Class.forName(name).newInstance());
                } catch (Exception e) {
                    throw new UnsupportedOperationException("Cannot load driver class", e);
                }
            }
        };
    }
}
