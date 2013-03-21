package com.googlecode.utterlyidle.migrations.bootstrap;

import com.googlecode.utterlyidle.migrations.Migration;
import com.googlecode.utterlyidle.migrations.Migrator;
import com.googlecode.utterlyidle.migrations.ModuleName;
import com.googlecode.utterlyidle.migrations.log.MigrationLogItem;

public class MigrationLogBootstrapper implements Migrator {
    private final Migrator decorated;
    private final Runnable bootstrapper;

    public MigrationLogBootstrapper(Migrator decorated, Bootstrapper bootstrapper) {
        this.decorated = decorated;
        this.bootstrapper = bootstrapper;
    }

    public Iterable<MigrationLogItem> migrate(Iterable<Migration> migrations, final ModuleName moduleName) {
        bootstrapper.run();
        return decorated.migrate(migrations, moduleName);
    }
}
