package com.googlecode.propidle.client;

class ReloadPropertiesRunnable implements Runnable {
    private final DynamicProperties dynamicProperties;

    public ReloadPropertiesRunnable(DynamicProperties dynamicProperties) {
        this.dynamicProperties = dynamicProperties;
    }

    public void run() {
        try {
            dynamicProperties.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
