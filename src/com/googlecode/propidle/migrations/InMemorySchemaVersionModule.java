package com.googlecode.propidle.migrations;

import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

public class InMemorySchemaVersionModule implements RequestScopedModule {
    private InMemorySchemaVersionModule() {
    }

    public Module addPerRequestObjects(Container container) {
        container.add(SchemaVersion.class, InMemorySchemaVersion.class);
        return this;
    }

    public static InMemorySchemaVersionModule inMemorySchemaVersionModule() {
        return new InMemorySchemaVersionModule();
    }
}
