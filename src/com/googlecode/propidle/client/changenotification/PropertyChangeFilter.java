package com.googlecode.propidle.client.changenotification;

import com.googlecode.propidle.properties.PropertyComparison;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import com.googlecode.propidle.client.PropertyChangeListener;
import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Sequences.sequence;

public class PropertyChangeFilter implements PropertyChangeListener {
    private final PropertyChangeListener decorated;
    private final Predicate<PropertyComparison> filter;

    public static PropertyChangeFilter filterPropertyChanges(PropertyChangeListener decorated, PropertyName propertyName) {
        return filterPropertyChanges(decorated, sequence(propertyName));
    }

    public static PropertyChangeFilter filterPropertyChanges(PropertyChangeListener decorated, Iterable<PropertyName> propertyNames) {
        return filterPropertyChanges(decorated, PropertyComparisonPredicates.propertyNameIn(propertyNames));
    }

    public static PropertyChangeFilter filterPropertyChanges(PropertyChangeListener decorated, Predicate<PropertyComparison>... filters) {
        LogicalPredicate<PropertyComparison> comparisonAndPredicate = and(filters);
        return new PropertyChangeFilter(decorated, comparisonAndPredicate);
    }

    protected PropertyChangeFilter(PropertyChangeListener decorated, Predicate<PropertyComparison> filter) {
        this.decorated = decorated;
        this.filter = filter;
    }

    public void propertiesHaveChanged(PropertyChangeEvent event) {
        if(sequence(event.changes()).filter(filter).isEmpty()) return;
        decorated.propertiesHaveChanged(event);
    }
}
