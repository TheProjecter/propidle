package com.googlecode.propidle.server;

import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.utterlyidle.ServerActivator;

import static com.googlecode.utterlyidle.ServerConfiguration.defaultConfiguration;

public class DemoServer {
    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.valueOf(args[0]) : 8000;
        new PropidleServerActivator(new TestPropertiesApplication(), defaultConfiguration().port(port)).call();
    }
}
