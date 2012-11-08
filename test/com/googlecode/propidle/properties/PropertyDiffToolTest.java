package com.googlecode.propidle.properties;

import org.junit.Test;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.propidle.properties.PropertyComparison.*;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;

public class PropertyDiffToolTest {
    private final PropertyDiffTool diff = new PropertyDiffTool();

    @Test
    public void shouldFindUpdatedProperties() {
        assertThat(
                diff.diffs(
                        properties(pair("some.property", "old value")),
                        properties(pair("some.property", "new value"))),
                hasExactly(changedProperty(propertyName("some.property"), propertyValue("old value"), propertyValue("new value"))));
    }

    @Test
    public void shouldFindUnchanged() {
        assertThat(
                diff.diffs(
                        properties(pair("some.property", "value")),
                        properties(pair("some.property", "value"))),
                hasExactly(unchangedProperty(propertyName("some.property"), propertyValue("value"))));
    }

    @Test
    public void shouldFindRemovedProperties() {
        assertThat(
                diff.diffs(
                        properties(pair("some.property", "old value")),
                        properties()),
                hasExactly(removedProperty(propertyName("some.property"), propertyValue("old value"))));
    }

    @Test
    public void shouldFindNewProperties() {
        assertThat(
                diff.diffs(
                        properties(),
                        properties(pair("some.property", "new value"))),
                hasExactly(createdProperty(propertyName("some.property"), propertyValue("new value"))));
    }
}
