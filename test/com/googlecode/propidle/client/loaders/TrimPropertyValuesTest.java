package com.googlecode.propidle.client.loaders;

import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.client.loaders.TrimPropertyValues.trimPropertyValues;
import static com.googlecode.totallylazy.Callables.returns;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TrimPropertyValuesTest {
    private static final String PROPERTY_KEY = "key";
    private static final String PROPERTY_VALUE = "value";
    private Properties expectedProperties;

    @Before
    public void setUp() {
        expectedProperties = new Properties();
        expectedProperties.setProperty(PROPERTY_KEY, PROPERTY_VALUE);
    }

    @Test
    public void shouldTrimPrefix() throws Exception {
        assertTrimsPropertyValues(" " + PROPERTY_VALUE);
    }

    @Test
    public void shouldTrimSuffix() throws Exception {
        assertTrimsPropertyValues(PROPERTY_VALUE + " ");
    }

    @Test
    public void shouldTrimPrefixAndSuffix() throws Exception {
        assertTrimsPropertyValues(" " + PROPERTY_VALUE + " ");
    }

    @Test
    public void shouldTrimTabs() throws Exception {
        assertTrimsPropertyValues(PROPERTY_VALUE + "\t");
    }

    @Test
    public void shouldTrimNewLines() throws Exception {
        assertTrimsPropertyValues(PROPERTY_VALUE + "\n");
    }

    private void assertTrimsPropertyValues(String propertyValue) throws Exception {
        Properties properties = new Properties();
        properties.setProperty(PROPERTY_KEY, propertyValue);
        Callable<Properties> propertiesCallable = returns(properties);

        assertThat(trimPropertyValues(propertiesCallable).call(), equalTo(expectedProperties));
    }
}
