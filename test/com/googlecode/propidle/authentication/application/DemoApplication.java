package com.googlecode.propidle.authentication.application;

import com.googlecode.utterlyidle.RestApplication;
import com.googlecode.propidle.security.SecurityModule;
import com.googlecode.utterlyidle.simpleframework.RestServer;

import static com.googlecode.utterlyidle.BasePath.basePath;
import static com.googlecode.utterlyidle.ServerConfiguration.defaultConfiguration;

public class DemoApplication extends RestApplication {

    public DemoApplication() {
        super(basePath("/"));
        add(new SecurityModule());
        add(new TestSupportModule());
    }

    public static void main(String[] args) throws Exception {
        new RestServer(new DemoApplication(), defaultConfiguration().port(8000));
    }
}
