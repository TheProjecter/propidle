package com.googlecode.propidle.persistence.jdbc;

import com.googlecode.propidle.persistence.SchemaVersion;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

public class SchemaVersionModule implements RequestScopedModule {
    private SchemaVersionModule() {
    }

    public Module addPerRequestObjects(Container container) {
        container.add(SchemaVersion.class, SchemaVersionFromMigrationLog.class);
        return this;
    }

    public static SchemaVersionModule schemaVersionModule() {
        return new SchemaVersionModule();
    }
}
