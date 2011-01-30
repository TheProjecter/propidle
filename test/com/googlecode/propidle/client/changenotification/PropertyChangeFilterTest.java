package com.googlecode.propidle.client.changenotification;

import com.googlecode.propidle.PropertyComparison;
import org.junit.Test;

import java.util.Properties;

import static com.googlecode.propidle.PropertyComparison.createdProperty;
import static com.googlecode.propidle.PropertyName.propertyName;
import static com.googlecode.propidle.PropertyName.propertyNames;
import static com.googlecode.propidle.PropertyValue.propertyValue;
import static com.googlecode.propidle.client.changenotification.PropertyChangeEvent.propertyChangeEvent;
import com.googlecode.propidle.client.changenotification.PropertyChangeFilter;
import com.googlecode.propidle.client.PropertyChangeListener;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class PropertyChangeFilterTest {
    private PropertyChangeListener decorated = mock(PropertyChangeListener.class);
    private Properties properties = new Properties();

    @Test
    public void filtersPropertyChangesByPredicate() {
        PropertyChangeFilter filter = PropertyChangeFilter.filterPropertyChanges(decorated, propertyNames("shambles"));

        PropertyComparison matchingChange = createdProperty(propertyName("shambles"), propertyValue("moo"));
        PropertyComparison nonMatchingChange = createdProperty(propertyName("not.shambles"), propertyValue("moo"));

        filter.propertiesHaveChanged(propertyChangeEvent(properties, matchingChange));
        verify(decorated).propertiesHaveChanged(propertyChangeEvent(properties, matchingChange));

        filter.propertiesHaveChanged(propertyChangeEvent(properties, nonMatchingChange));
        verify(decorated, never()).propertiesHaveChanged(propertyChangeEvent(properties, nonMatchingChange));
    }
}
