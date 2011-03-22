package com.googlecode.propidle.server;

import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.utterlyidle.simpleframework.RestServer;

import static com.googlecode.utterlyidle.BasePath.basePath;

public class DemoServer {
    public static void main(String[] args) throws Exception {
        TestPropertiesApplication application = new TestPropertiesApplication();

        int port = args.length > 0 ? Integer.valueOf(args[0]) : 8000;
        new RestServer(port, basePath("/"), application);
    }

}
