package com.googlecode.propidle.versioncontrol;

import com.googlecode.propidle.properties.PropertyComparison;
import org.junit.Test;

import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.propidle.properties.PropertyComparison.changedProperty;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PropertyChangeTest {
    @Test
    public void implementsEquality() {
        PropertyComparison base = changedProperty(propertyName("some.property"), propertyValue("old value"), propertyValue("new value"));
        PropertyComparison equivalent = changedProperty(propertyName("some.property"), propertyValue("old value"), propertyValue("new value"));
        
        assertThat(base, is(equalTo(equivalent)));
    }
}
