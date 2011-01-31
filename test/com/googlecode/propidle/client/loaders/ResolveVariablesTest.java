package com.googlecode.propidle.client.loaders;


import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.Callable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ResolveVariablesTest {
    private static final String ENVIRONMENT_VARIABLE_VALUE = "Jdbc Url";
    private static final String ENVIRONMENT_VARIABLE_NAME = "JDBC_URL";
    private static final String NAME = "some.property";
    private static final String UNKNOWN_ENVIRONMENT_VARIABLE_NAME = "UNKNOWN";

    @Test
    public void shouldResolveEnvironmentVariable() throws Exception {
        Properties environmentVariables = ResolveVariables.resolveProperties(environmentProperties(), ENVIRONMENT_VARIABLE_NAME).call();
        assertThat(environmentVariables.getProperty(NAME), is(ENVIRONMENT_VARIABLE_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoPropertyNamesSpecified() throws Exception {
        ResolveVariables.resolveProperties(environmentProperties());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUnknownPropertySpecified() throws Exception {
        ResolveVariables.resolveProperties(environmentProperties(), UNKNOWN_ENVIRONMENT_VARIABLE_NAME).call();
    }

    private static Callable<Properties> environmentProperties() {
        final Properties environmentVariables = new Properties();
        environmentVariables.setProperty(NAME, "${" + ENVIRONMENT_VARIABLE_NAME + "}");
        environmentVariables.setProperty(ENVIRONMENT_VARIABLE_NAME, ENVIRONMENT_VARIABLE_VALUE);
        return propertiesOf(environmentVariables);
    }

    private static Callable<Properties> propertiesOf(final Properties properties) {
        return new Callable<Properties>() {
            public Properties call() throws Exception {
                return properties;
            }
        };
    }
}