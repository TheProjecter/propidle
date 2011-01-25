package com.googlecode.propidle.versioncontrol;

import com.googlecode.propidle.PropertyComparison;
import com.googlecode.propidle.PropertyName;
import org.junit.Test;

import static com.googlecode.propidle.PropertyValue.propertyValue;
import static com.googlecode.propidle.PropertyComparison.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PropertyComparisonTest {
    private static final PropertyName SOME_NAME = PropertyName.propertyName("");

    @Test
    public void shouldGiveStatus() {
        assertThat(changedProperty(SOME_NAME, propertyValue("old"), propertyValue("new")).status(), is(PropertyComparison.Status.UPDATED));
        assertThat(newProperty(SOME_NAME, propertyValue("new")).status(), is(PropertyComparison.Status.NEW));
        assertThat(removedProperty(SOME_NAME, propertyValue("old")).status(), is(PropertyComparison.Status.REMOVED));
        assertThat(unchangedProperty(SOME_NAME, propertyValue("value")).status(), is(PropertyComparison.Status.UNCHANGED));
    }
}
