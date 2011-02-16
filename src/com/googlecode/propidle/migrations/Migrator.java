package com.googlecode.propidle.migrations;

import com.googlecode.propidle.migrations.history.MigrationEvent;

public interface Migrator {
    Iterable<MigrationEvent> migrate(Iterable<Migration> migrations);
}
