package com.googlecode.propidle.diff;

import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.propidle.properties.PropertyValue;
import com.googlecode.totallylazy.Option;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.option;

public class PropertyComparison {
    private final PropertyName propertyName;
    private final Option<PropertyValue> previous;
    private final Option<PropertyValue> updated;

    public static PropertyComparison createdProperty(PropertyName propertyName, PropertyValue value) {
        return changedProperty(propertyName, null, value);
    }

    public static PropertyComparison removedProperty(PropertyName propertyName, PropertyValue previousValue) {
        return changedProperty(propertyName, previousValue, null);
    }

    public static PropertyComparison unchangedProperty(PropertyName propertyName, PropertyValue value) {
        return changedProperty(propertyName, value, value);
    }

    public static PropertyComparison changedProperty(PropertyName propertyName, PropertyValue previous, PropertyValue updated) {
        return new PropertyComparison(propertyName, option(previous), option(updated));
    }

    protected PropertyComparison(PropertyName propertyName, Option<PropertyValue> previous, Option<PropertyValue> updated) {
        if (propertyName == null) throw new NullArgumentException("propertyName");
        if (previous == null) throw new NullArgumentException("previous");
        if (updated == null) throw new NullArgumentException("updated");
        this.propertyName = propertyName;
        this.previous = previous;
        this.updated = updated;
    }

    public PropertyName propertyName() {
        return propertyName;
    }

    public Option<PropertyValue> previous() {
        return previous;
    }

    public Option<PropertyValue> updated() {
        return updated;
    }

    @Override
    public String toString() {
        return String.format("%s:%s --> %s", propertyName, previous, updated);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyComparison that = (PropertyComparison) o;

        if (!previous.equals(that.previous)) return false;
        if (!propertyName.equals(that.propertyName)) return false;
        if (!updated.equals(that.updated)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = propertyName.hashCode();
        result = 31 * result + previous.hashCode();
        result = 31 * result + updated.hashCode();
        return result;
    }

    public Status status() {
        if(updated.isEmpty()) return Status.REMOVED;
        if(previous.isEmpty()) return Status.NEW;
        if(!previous.equals(updated)) return Status.UPDATED;
        return Status.UNCHANGED;
    }

    public enum Status{
        UNCHANGED,
        UPDATED,
        NEW,
        REMOVED
    }
}
