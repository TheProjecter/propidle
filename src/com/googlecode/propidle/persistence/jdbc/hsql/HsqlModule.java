package com.googlecode.propidle.persistence.jdbc.hsql;

import com.googlecode.propidle.persistence.RecordLock;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

public class HsqlModule implements RequestScopedModule {
    public Module addPerRequestObjects(Container container) {
        container.add(RecordLock.class, HsqlRecordLock.class);
        return this;
    }
}
