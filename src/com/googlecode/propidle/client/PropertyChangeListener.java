package com.googlecode.propidle.client;

import com.googlecode.propidle.client.changenotification.PropertyChangeEvent;

import java.util.EventListener;

public interface PropertyChangeListener extends EventListener {
    public void propertiesHaveChanged(PropertyChangeEvent event);
}
