package com.googlecode.propidle.migrations.bootstrap;

import com.googlecode.propidle.migrations.Migration;
import com.googlecode.propidle.migrations.Migrator;
import com.googlecode.propidle.migrations.log.MigrationLogItem;

public class MigrationLogBootstrapper implements Migrator {
    private final Migrator decorated;
    private final Runnable bootstrapper;

    public MigrationLogBootstrapper(Migrator decorated, Bootstrapper bootstrapper) {
        this.decorated = decorated;
        this.bootstrapper = bootstrapper;
    }

    public Iterable<MigrationLogItem> migrate(Iterable<Migration> migrations) {
        bootstrapper.run();
        return decorated.migrate(migrations);
    }
}
