package com.googlecode.propidle.migrations.history;

import com.googlecode.totallylazy.Option;
import com.googlecode.propidle.migrations.MigrationNumber;

public interface MigrationHistory {
    Option<MigrationEvent> get(MigrationNumber migrationNumber);

    Iterable<MigrationEvent> add(Iterable<MigrationEvent> events);
}
