package com.googlecode.propidle.persistence.jdbc.oracle;

import com.googlecode.propidle.persistence.jdbc.DatabaseVersionCheck;
import com.googlecode.propidle.status.StatusChecks;
import com.googlecode.propidle.status.ConnectionDetailsCheck;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import com.googlecode.propidle.persistence.RecordLock;

public class OracleModule implements RequestScopedModule {
    public Module addPerRequestObjects(Container container) {
        container.add(RecordLock.class, OracleRecordLock.class);
        registerStatusChecks(container);
        return this;
    }

    private void registerStatusChecks(Container container) {
        if (container.contains(StatusChecks.class)) {
            container.get(StatusChecks.class).add(DatabaseVersionCheck.class);
            container.get(StatusChecks.class).add(ConnectionDetailsCheck.class);
        }
    }
}
