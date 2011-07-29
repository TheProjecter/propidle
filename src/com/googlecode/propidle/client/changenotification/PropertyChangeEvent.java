package com.googlecode.propidle.client.changenotification;

import com.googlecode.propidle.properties.PropertyComparison;
import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.Properties;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;

public class PropertyChangeEvent {
    private final Properties updatedProperties;
    private final Sequence<PropertyComparison> changes;

    public static PropertyChangeEvent propertyChangeEvent(Properties updatedProperties, PropertyComparison... changes) {
        return propertyChangeEvent(updatedProperties, sequence(changes));
    }
    public static PropertyChangeEvent propertyChangeEvent(Properties updatedProperties, Iterable<PropertyComparison> changes) {
        return new PropertyChangeEvent(updatedProperties, changes);
    }

    protected PropertyChangeEvent(Properties updatedProperties, Iterable<PropertyComparison> changes) {
        if(updatedProperties==null)throw new NullArgumentException("updatedProperties");
        this.updatedProperties = updatedProperties;
        this.changes = changes==null? Sequences.<PropertyComparison>empty():sequence(changes);
    }

    public Properties updatedProperties(){
        return updatedProperties;
    }
    public Iterable<PropertyComparison> changes(){
        return changes;
    }

    public boolean containsChanges() {
        return !changes.isEmpty();
    }

    @Override
    public String toString() {
        return changes.toString("[",",","]");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyChangeEvent event = (PropertyChangeEvent) o;

        if (!hasExactly(event.changes).matches(changes)) return false;
        if (!updatedProperties.equals(event.updatedProperties)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = updatedProperties.hashCode();
        result = 31 * result + changes.hashCode();
        return result;
    }
}
