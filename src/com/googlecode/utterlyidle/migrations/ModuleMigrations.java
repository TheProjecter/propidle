package com.googlecode.utterlyidle.migrations;

public interface ModuleMigrations {
    ModuleName moduleName();

    Migrations migrations();
}
