package com.googlecode.propidle.server;

import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.propidle.scheduling.ScheduleResource;
import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.Server;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;

import static com.googlecode.utterlyidle.ServerConfiguration.defaultConfiguration;

public class DemoServer {
    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.valueOf(args[0]) : 8000;
        TestPropertiesApplication application = new TestPropertiesApplication();
        new ServerActivator(application, defaultConfiguration().port(port)).call();
        application.handle(RequestBuilder.post(ScheduleResource.NAME).withForm("taskName", "rebuildIndex").build());
    }
}
