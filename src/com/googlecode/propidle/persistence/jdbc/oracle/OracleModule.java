package com.googlecode.propidle.persistence.jdbc.oracle;

import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import com.googlecode.propidle.persistence.RecordLock;

public class OracleModule implements RequestScopedModule {
    public Module addPerRequestObjects(Container container) {
        container.add(RecordLock.class, OracleRecordLock.class);
        return this;
    }
}
