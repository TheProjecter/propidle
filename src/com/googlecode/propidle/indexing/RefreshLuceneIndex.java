package com.googlecode.propidle.indexing;

import com.googlecode.propidle.ApplicationStartupBarrier;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.RequestBuilder;

public class RefreshLuceneIndex implements Runnable {

    private final ApplicationStartupBarrier applicationStartupBarrier;
    private final Application application;

    public RefreshLuceneIndex(ApplicationStartupBarrier applicationStartupBarrier, Application application){
        this.applicationStartupBarrier = applicationStartupBarrier;
        this.application = application;
    }

    public void run() {
        try {
            applicationStartupBarrier.waitUntilAppIStartUp();
            application.handle(RequestBuilder.post("/" + RebuildIndexResource.NAME).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
