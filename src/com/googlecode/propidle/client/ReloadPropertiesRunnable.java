package com.googlecode.propidle.client;

import com.googlecode.propidle.client.handlers.FailureHandler;

class ReloadPropertiesRunnable implements Runnable {
    private final DynamicProperties dynamicProperties;
    private final FailureHandler failureHandler;

    public ReloadPropertiesRunnable(DynamicProperties dynamicProperties, FailureHandler failureHandler) {
        this.dynamicProperties = dynamicProperties;
        this.failureHandler = failureHandler;
    }

    public void run() {
        try {
            dynamicProperties.reload();
        } catch (Exception e) {
            failureHandler.handle(e);
        }
    }

}
