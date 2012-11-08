package com.googlecode.propidle.properties;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.googlecode.propidle.properties.Properties.toPair;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.propidle.properties.PropertyComparison.createdProperty;
import static com.googlecode.propidle.properties.PropertyComparison.changedProperty;
import static com.googlecode.propidle.properties.PropertyComparison.removedProperty;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;

public class PropertyDiffTool {
    public Iterable<PropertyComparison> diffs(Properties previous, Properties updated) {
        List<PropertyComparison> comparisons = new ArrayList<PropertyComparison>();
        comparisons = sequence(updated.entrySet()).map(toPair()).filter(not(in(previous))).fold(comparisons, newProperties());
        comparisons = sequence(previous.entrySet()).map(toPair()).filter(not(in(updated))).fold(comparisons, removedProperties());
        comparisons = sequence(previous.entrySet()).map(toPair()).filter(in(updated)).fold(comparisons, changedProperties(updated));
        return comparisons;
    }

    private Callable2<? super List<PropertyComparison>, ? super Pair<String, String>, List<PropertyComparison>> changedProperties(final Properties updated) {
        return new Callable2<List<PropertyComparison>, Pair<String, String>, List<PropertyComparison>>() {
            public List<PropertyComparison> call(List<PropertyComparison> propertyComparisons, Pair<String, String> previousProperty) throws Exception {
                propertyComparisons.add(changedProperty(
                        propertyName(previousProperty.first()),
                        propertyValue(previousProperty.second()),
                        propertyValue(updated.getProperty(previousProperty.first()))));
                return propertyComparisons;
            }
        };
    }

    private Callable2<? super List<PropertyComparison>, ? super Pair<String, String>, List<PropertyComparison>> newProperties() {
        return new Callable2<List<PropertyComparison>, Pair<String, String>, List<PropertyComparison>>() {
            public List<PropertyComparison> call(List<PropertyComparison> propertyComparisons, Pair<String, String> property) throws Exception {
                propertyComparisons.add(createdProperty(PropertyName.propertyName(property.first()), PropertyValue.propertyValue(property.second())));
                return propertyComparisons;
            }
        };
    }

    private Callable2<? super List<PropertyComparison>, ? super Pair<String,String>, List<PropertyComparison>> removedProperties() {
        return new Callable2<List<PropertyComparison>, Pair<String,String>, List<PropertyComparison>>() {
            public List<PropertyComparison> call(List<PropertyComparison> propertyComparisons, Pair<String,String> property) throws Exception {
                propertyComparisons.add(removedProperty(propertyName(property.first()), propertyValue(property.second())));
                return propertyComparisons;
            }
        };
    }

    private Predicate<? super Pair<String,String>> in(final Properties properties) {
        return new Predicate<Pair<String,String>>() {
            public boolean matches(Pair<String,String> entry) {
                return properties.containsKey(entry.first());
            }
        };
    }

    public static Predicate<? super PropertyComparison> propertyValueChanged() {
        return new Predicate<PropertyComparison>() {
            public boolean matches(PropertyComparison propertyComparison) {
                return ! propertyComparison.previous().equals(propertyComparison.updated());
            }
        };
    }
}
