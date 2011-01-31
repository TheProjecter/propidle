package com.googlecode.propidle.versioncontrol;

import com.googlecode.propidle.PropertyComparison;
import org.junit.Test;

import static com.googlecode.propidle.PropertyName.propertyName;
import static com.googlecode.propidle.PropertyValue.propertyValue;
import static com.googlecode.propidle.PropertyComparison.changedProperty;
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
