package com.googlecode.propidle.client;

import com.googlecode.propidle.client.changenotification.PropertyChangeEvent;
import com.googlecode.propidle.properties.PropertyComparison;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.totallylazy.Callable1;
import org.junit.Test;

import java.util.Properties;

import static com.googlecode.propidle.client.changenotification.PropertyChangeEvent.propertyChangeEvent;
import static com.googlecode.propidle.properties.PropertyComparison.changedProperty;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class SpecificPropertyChangeListenerTest {

    private Properties someProperties = new Properties();
    private StubCallable propertyChangeAction = new StubCallable();
    private PropertyName interestingProperty = propertyName("interesting property");
    private SpecificPropertyChangeListener interestingPropertyChangeListener = new SpecificPropertyChangeListener(interestingProperty, propertyChangeAction);


    @Test
    public void shouldExecuteCallableWhenSpecifiedPropertyHasChanged() {
        interestingPropertyChangeListener.propertiesHaveChanged(propertyChangedEvent(interestingProperty));
        assertThat(propertyChangeAction.propertyComparison.propertyName(), is(equalTo(interestingProperty)));
    }

    @Test
    public void shouldNotExecuteCallableWhenIrrelevantChangeHappens() {
        interestingPropertyChangeListener.propertiesHaveChanged(propertyChangedEvent(propertyName("irrelevant property")));
        assertThat(propertyChangeAction.wasCalled(), is(false));

    }

    private PropertyChangeEvent propertyChangedEvent(final PropertyName propertyName) {
        return propertyChangeEvent(someProperties, changedProperty(propertyName, propertyValue("old value"), propertyValue("new value")));
    }


    private class StubCallable implements Callable1<PropertyComparison, Void> {
        private PropertyComparison propertyComparison;
        private boolean called=false;

        public Void call(PropertyComparison propertyComparison) throws Exception {
            this.propertyComparison = propertyComparison;
            called=true;
            return null;
        }

        public boolean wasCalled(){
            return called;
        }
    }
}
