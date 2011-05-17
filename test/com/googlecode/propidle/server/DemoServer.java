package com.googlecode.propidle.server;

import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.utterlyidle.ServerConfiguration;
import com.googlecode.utterlyidle.jetty.RestApplicationActivator;
import com.googlecode.utterlyidle.simpleframework.RestServer;

import static com.googlecode.utterlyidle.BasePath.basePath;
import static com.googlecode.utterlyidle.ServerConfiguration.serverConfiguration;

public class DemoServer {
    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.valueOf(args[0]) : 8000;
        new RestServer(new RestApplicationActivator(new TestPropertiesApplication()), serverConfiguration().port(port));
    }

}
