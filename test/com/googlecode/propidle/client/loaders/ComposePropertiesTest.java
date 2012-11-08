package com.googlecode.propidle.client.loaders;

import org.junit.Test;

import java.util.Properties;

import static com.googlecode.propidle.client.loaders.ComposeProperties.composeProperties;
import static com.googlecode.propidle.client.properties.Properties.properties;
import static com.googlecode.totallylazy.Callables.returns;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ComposePropertiesTest {
    private Properties properties1 = properties();
    private Properties properties2 = properties();

    @Test
    public void shouldMergeSystemProperties() throws Exception {
        properties1.setProperty("should.be.overridden", "NOT OVERRIDDEN!!");
        properties2.setProperty("should.be.overridden", "overridden");

        properties1.setProperty("from.base", "base");
        properties2.setProperty("from.overrides", "overrides");

        Properties composedProperties = composeProperties(returns(properties1), returns(properties2)).call();

        assertThat(composedProperties.getProperty("should.be.overridden"), is("overridden"));
        assertThat(composedProperties.getProperty("from.base"), is("base"));
        assertThat(composedProperties.getProperty("from.overrides"), is("overrides"));
    }
}
