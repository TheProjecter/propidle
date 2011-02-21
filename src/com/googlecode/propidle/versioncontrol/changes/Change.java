package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.diff.PropertyComparison;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.propidle.properties.PropertyValue;
import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Callable2;

import java.util.Properties;

public class Change {
    private final RevisionNumber revisionNumber;
    private final PropertiesPath propertiesPath;
    private final PropertyComparison comparison;

    public static Change change(RevisionNumber revisionNumber, PropertiesPath path, PropertyComparison comparison) {
        return new Change(revisionNumber, path, comparison);
    }

    public Change(RevisionNumber revisionNumber, PropertiesPath propertiesPath, PropertyComparison change) {
        if (revisionNumber == null) throw new NullArgumentException("revisionNumber");
        if (propertiesPath == null) throw new NullArgumentException("id");
        if (change == null) throw new NullArgumentException("change");

        this.revisionNumber = revisionNumber;
        this.propertiesPath = propertiesPath;
        this.comparison = change;
    }

    public PropertiesPath propertiesPath() {
        return propertiesPath;
    }

    public RevisionNumber revisionNumber() {
        return revisionNumber;
    }

    public PropertyComparison change() {
        return comparison;
    }

    public PropertyName propertyName() {
        return comparison.propertyName();
    }

    public Option<PropertyValue> previous() {
        return comparison.previous();
    }

    public Option<PropertyValue> updated() {
        return comparison.updated();
    }

    public PropertyComparison.Status status() {
        return comparison.status();
    }

    public Properties applyTo(Properties properties) {
        final Option<PropertyValue> updatedValue = comparison.updated();
        if (updatedValue.isEmpty()) {
            properties.remove(propertyName().value());
        } else {
            properties.put(propertyName().value(), updatedValue.get().value());
        }
        return properties;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", revisionNumber, propertiesPath, comparison);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Change change = (Change) o;

        if (!comparison.equals(change.comparison)) return false;
        if (!propertiesPath.equals(change.propertiesPath)) return false;
        if (!revisionNumber.equals(change.revisionNumber)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = comparison.hashCode();
        result = 31 * result + revisionNumber.hashCode();
        result = 31 * result + propertiesPath.hashCode();
        return result;
    }

    public static Callable2<? super Properties, ? super Change, Properties> applyChange() {
        return new Callable2<Properties, Change, Properties>() {
            public Properties call(Properties properties, Change change) throws Exception {
                return change.applyTo(properties);
            }
        };
    }
}
