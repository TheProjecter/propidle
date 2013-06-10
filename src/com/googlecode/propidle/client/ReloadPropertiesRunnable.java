package com.googlecode.propidle.client;

import com.googlecode.propidle.client.logging.Logger;

import static com.googlecode.propidle.client.logging.Message.message;
import static com.googlecode.propidle.util.Exceptions.stackTraceToString;

class ReloadPropertiesRunnable implements Runnable {
    private final DynamicProperties dynamicProperties;
    private final Logger logger;

    public ReloadPropertiesRunnable(DynamicProperties dynamicProperties, Logger logger) {
        this.dynamicProperties = dynamicProperties;
        this.logger = logger;
    }

    public void run() {
        try {
            dynamicProperties.reload();
        } catch (Exception e) {
            logger.log(message(stackTraceToString(e)));
        }
    }
}
