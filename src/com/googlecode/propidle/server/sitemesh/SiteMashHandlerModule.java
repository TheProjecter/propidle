package com.googlecode.propidle.server.sitemesh;

import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.sitemesh.SiteMeshHandler;
import com.googlecode.yadic.Container;

public class SiteMashHandlerModule implements RequestScopedModule {
    public Container addPerRequestObjects(Container container) throws Exception {
        return container.decorate(HttpHandler.class, SiteMeshHandler.class);
    }

}
