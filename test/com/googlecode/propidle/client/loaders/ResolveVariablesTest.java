package com.googlecode.propidle.client.loaders;


import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.Callable;

import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ResolveVariablesTest {
    private static final String ENVIRONMENT_VARIABLE_VALUE = "Jdbc Url";
    private static final String ENVIRONMENT_VARIABLE_NAME = "JDBC_URL";
    private static final String NAME = "some.property";
    private static final String NAME1 = "other.property";
    private static final String UNDEFINED_ENVIRONMENT_VARIABLE_NAME = "UNDEFINED";
    private static final String NOT_USED_ENVIRONMENT_VARIABLE_NAME = "UNUSED";

    @Test
    public void shouldResolveEnvironmentVariable() throws Exception {
        Properties environmentVariables = ResolveVariables.resolveProperties(environmentProperties(), ENVIRONMENT_VARIABLE_NAME).call();
        assertThat(environmentVariables.getProperty(NAME), is(ENVIRONMENT_VARIABLE_VALUE));
    }

    @Test
    public void shouldNotThrowExceptionWhenUndefinedPropertySpecifiedByDefaultAllowingApplicationToStart() throws Exception {
        try {
            Properties properties = ResolveVariables.resolveProperties(environmentProperties(), UNDEFINED_ENVIRONMENT_VARIABLE_NAME).call();
            assertThat(properties.getProperty(ENVIRONMENT_VARIABLE_NAME), is(ENVIRONMENT_VARIABLE_VALUE));
        } catch (Exception e) {
            fail("Unexpected exception " + e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUndefinedPropertySpecifiedIfDesired() throws Exception {
            ResolveVariables.resolveProperties(environmentProperties(), PropertyChecker.constructors.mandatory(), UNDEFINED_ENVIRONMENT_VARIABLE_NAME).call();
    }

    @Test
    public void shouldNotThrowExceptionWhenUnusedVariableIsMissing() {
        try {
            ResolveVariables.resolveProperties(environmentProperties(), NOT_USED_ENVIRONMENT_VARIABLE_NAME).call();
        } catch (Exception e) {
            fail("Unexpected exception " + e);
        }
    }

    private static Callable<Properties> environmentProperties() {
        final Properties environmentVariables = new Properties();
        environmentVariables.setProperty(NAME1, "${" + UNDEFINED_ENVIRONMENT_VARIABLE_NAME + "}");
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