package com.googlecode.propidle.client.changenotification;

import com.googlecode.propidle.client.PropertyChangeListener;
import static com.googlecode.propidle.client.changenotification.PropertyChangeEvent.propertyChangeEvent;
import com.googlecode.propidle.diff.PropertyComparison;
import static com.googlecode.propidle.diff.PropertyComparison.createdProperty;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyName.propertyNames;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.Properties;

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
