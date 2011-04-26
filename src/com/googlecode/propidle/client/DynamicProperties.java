package com.googlecode.propidle.client;

import com.googlecode.propidle.client.changenotification.PropertyChangeEvent;

import java.util.Properties;

public interface DynamicProperties {
    PropertyChangeEvent reload() throws Exception;
    Properties snapshot();
}
