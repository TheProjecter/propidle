package com.googlecode.propidle;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.propidle.properties.PropertyValue;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static org.mockito.Mockito.*;

public class PropertyTriggeredExecutorTest {
    private PropertyName testProperty = propertyName("test.property");
    private PropertyValue defaultValue = PropertyValue.propertyValue("some value");
    private PropertyValue propertyValue = PropertyValue.propertyValue("value");
    private Properties properties = new Properties();
    private DynamicProperties dynamicProperties;
    private PropertyTriggeredExecutor propertyTriggeredExecutor;
    private Callable1 callable = Mockito.mock(Callable1.class);

    @Before
    public void setUp() throws Exception {
        properties.setProperty(testProperty.value(), propertyValue.value());
        dynamicProperties = DynamicProperties.load(stubPropertyLoader());
        propertyTriggeredExecutor = new PropertyTriggeredExecutor(dynamicProperties);
    }

    @Test
    public void shouldExecuteCallableWithPropertyValue() throws Exception {
        propertyTriggeredExecutor.register(testProperty, callable, defaultValue);

        verify(callable, times(1)).call(propertyValue);

    }

    @Test
    public void shouldExecuteCallableWithDefaultValueWhenPropertyValueIsMissing() throws Exception {
        propertyTriggeredExecutor.register(propertyName("missing property"), callable, defaultValue);

        verify(callable, times(1)).call(defaultValue);
    }

    @Test
    public void shouldExecuteCallableWithNewPropertyValueWhenPropertyIsChanging() throws Exception {
        registerExecutionFor(testProperty);

        PropertyValue updatedPropertyValue = PropertyValue.propertyValue("updated");
        properties.setProperty(testProperty.value(), updatedPropertyValue.value());
        dynamicProperties.reload();

        verify(callable).call(updatedPropertyValue);
    }

    @Test
    public void shouldNotExecuteCallableWhenOtherPropertyIsChanging() throws Exception {
        registerExecutionFor(testProperty);

        properties.setProperty("other property", "different value");
        dynamicProperties.reload();

        verify(callable, never()).call(any());
    }

    private void registerExecutionFor(final PropertyName testProperty) {
        propertyTriggeredExecutor.register(testProperty, callable, defaultValue);
        reset(callable);
    }

    private Callable<Properties> stubPropertyLoader() {
        return new Callable<Properties>() {
            public Properties call() throws Exception {
                return com.googlecode.propidle.properties.Properties.properties(properties);
            }
        };
    }
}
