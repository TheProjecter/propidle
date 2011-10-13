package com.googlecode.propidle.client;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.client.DynamicProperties.load;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DynamicPropertiesTest {

    private final Callable<Properties> propertiesLoader1 = Mockito.mock(Callable.class);
    private final Callable<Properties> propertiesLoader2 = Mockito.mock(Callable.class);
    private final Properties someProperties = someProperties();

    @Test
    public void loadPropertiesFromTheFirstCallableWhenAvailable() throws Exception {
        when(propertiesLoader1.call()).thenReturn(someProperties);
        assertThat(load(propertiesLoader1, propertiesLoader2).snapshot(), is(someProperties));
        verify(propertiesLoader2, never()).call();
    }

    @Test
    public void loadPropertiesFromTheFirstAvailableCallable() throws Exception {
        when(propertiesLoader1.call()).thenThrow(new RuntimeException());
        when(propertiesLoader2.call()).thenReturn(someProperties);
        
        assertThat(load(propertiesLoader1, propertiesLoader2).snapshot(), is(someProperties));
    }

    @Test(expected = RuntimeException.class)
    public void throwExceptionWhenNoPropertiesAreAvailable() throws Exception {
        when(propertiesLoader1.call()).thenThrow(new RuntimeException());
        when(propertiesLoader2.call()).thenThrow(new RuntimeException());

        load(propertiesLoader1, propertiesLoader2);
    }

    private Properties someProperties() {
        Properties properties = new Properties();
        properties.put("random", "stuff");
        return properties;
    }
}
