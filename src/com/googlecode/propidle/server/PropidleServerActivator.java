package com.googlecode.propidle.server;

import com.googlecode.utterlyidle.ServerActivator;

import java.util.concurrent.Callable;

public class PropidleServerActivator implements Callable<com.googlecode.utterlyidle.Server> {
    private final PropertiesApplication application;
    private final com.googlecode.utterlyidle.ServerConfiguration serverConfig;

    public PropidleServerActivator(PropertiesApplication application, com.googlecode.utterlyidle.ServerConfiguration serverConfig) {
        this.application = application;
        this.serverConfig = serverConfig;
    }

    public com.googlecode.utterlyidle.Server call() throws Exception {
        com.googlecode.utterlyidle.Server server = new ServerActivator(application, serverConfig).call();
        application.started();
        return server;
    }
}
