package com.googlecode.propidle.migrations;

public interface ModuleMigrations {
    ModuleName moduleName();

    Migrations migrations();
}
