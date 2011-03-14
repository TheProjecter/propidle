package com.googlecode.propidle.monitoring;

import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

public class MonitoringModule implements ApplicationScopedModule, RequestScopedModule {
    public Module addPerApplicationObjects(Container container) {
        container.add(HttpRequestCounter.class);
        return this;
    }

    public Module addPerRequestObjects(Container container) {
        container.decorate(HttpHandler.class, HttpRequestCountingHandler.class);
        return this;
    }
}
