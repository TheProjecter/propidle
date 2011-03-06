package com.googlecode.propidle.migrations;

import java.util.Iterator;

public class Migrations implements Iterable<Migration>{
    private final Iterable<Migration> migrations;

    public static Migrations migrations(Iterable<Migration> migrations) {
        return new Migrations(migrations);
    }

    protected Migrations(Iterable<Migration> migrations) {
        this.migrations = migrations;
    }

    public Iterator<Migration> iterator() {
        return migrations.iterator();
    }
}
