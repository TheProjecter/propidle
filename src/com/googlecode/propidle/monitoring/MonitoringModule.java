package com.googlecode.propidle.monitoring;

import com.googlecode.propidle.server.RegisterCountingMBeans;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

public class MonitoringModule implements ApplicationScopedModule, RequestScopedModule {
    public Container addPerApplicationObjects(Container container) {
        container.add(HttpRequestCounter.class);
        return container.add(RegisterCountingMBeans.class);
    }

    public Container addPerRequestObjects(Container container) {
        return container.decorate(HttpHandler.class, HttpRequestCountingHandler.class);
    }
}
