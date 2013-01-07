package com.googlecode.propidle;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.propidle.client.SpecificPropertyChangeListener;
import com.googlecode.propidle.properties.PropertyComparison;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.propidle.properties.PropertyValue;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callers;

import static com.googlecode.propidle.properties.PropertyValue.propertyValue;

public class PropertyTriggeredExecutor {

    private final DynamicProperties dynamicProperties;

    public PropertyTriggeredExecutor(DynamicProperties dynamicProperties) {
        this.dynamicProperties = dynamicProperties;
    }

    public void register(final PropertyName propertyName, final Callable1<? super PropertyValue, ?> callable, final PropertyValue defaultPropertyValue) {
        final String property = dynamicProperties.snapshot().getProperty(propertyName.value(), defaultPropertyValue.value());

        dynamicProperties.listen(new SpecificPropertyChangeListener(propertyName, call(callable, defaultPropertyValue)));
        Callers.call(callable, propertyValue(property));
    }

    private Callable1<PropertyComparison, Void> call(final Callable1<? super PropertyValue, ?> scheduledStuff, final PropertyValue defaultPropertyValue) {
        return new Callable1<PropertyComparison, Void>() {
            public Void call(PropertyComparison propertyComparison) throws Exception {
                scheduledStuff.call(propertyComparison.updated().getOrElse(defaultPropertyValue));
                return null;
            }
        };
    }

}
