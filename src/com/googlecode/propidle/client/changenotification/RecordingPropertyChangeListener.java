package com.googlecode.propidle.client.changenotification;

import com.googlecode.propidle.client.PropertyChangeListener;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class RecordingPropertyChangeListener implements PropertyChangeListener {
    private final List<PropertyChangeEvent> events = new ArrayList<PropertyChangeEvent>();

    public PropertyChangeEvent lastEvent() {
        return sequence(events).reverse().headOption().getOrNull();
    }

    public void propertiesHaveChanged(PropertyChangeEvent event) {
        this.events.add(event);
    }

    public List<PropertyChangeEvent> events() {
        return events;
    }
}
