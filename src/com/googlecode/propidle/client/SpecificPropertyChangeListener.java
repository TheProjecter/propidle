package com.googlecode.propidle.client;

import com.googlecode.propidle.client.changenotification.PropertyChangeEvent;
import com.googlecode.propidle.properties.PropertyComparison;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;

import static com.googlecode.totallylazy.Sequences.sequence;

public class SpecificPropertyChangeListener implements PropertyChangeListener {

    private final PropertyName propertyName;
    private final Callable1<PropertyComparison,Void> callable;

    public SpecificPropertyChangeListener(PropertyName propertyName, Callable1<PropertyComparison, Void> callable) {
        this.propertyName = propertyName;
        this.callable = callable;
    }

    public void propertiesHaveChanged(PropertyChangeEvent event) {
        sequence(event.changes()).filter(propertyWithName(propertyName)).headOption().map(callable);
    }

    private Predicate<PropertyComparison> propertyWithName(final PropertyName propertyName) {
        return new Predicate<PropertyComparison>() {
            public boolean matches(PropertyComparison propertyComparison) {
                return propertyName.equals(propertyComparison.propertyName());
            }
        };
    }

}
