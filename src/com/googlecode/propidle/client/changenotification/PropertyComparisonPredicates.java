package com.googlecode.propidle.client.changenotification;

import com.googlecode.propidle.PropertyComparison;
import com.googlecode.propidle.PropertyName;
import com.googlecode.totallylazy.Predicate;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.sequence;

public class PropertyComparisonPredicates {
    public static Predicate<PropertyComparison> propertyNameIn(final Iterable<PropertyName> propertyNames) {
        return new Predicate<PropertyComparison>() {
            public boolean matches(PropertyComparison other) {
                return !sequence(propertyNames).filter(is(other.propertyName())).isEmpty();
            }
        };
    }
}
