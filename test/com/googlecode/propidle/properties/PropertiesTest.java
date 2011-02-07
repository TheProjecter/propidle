package com.googlecode.propidle.properties;

import org.junit.Test;

import static com.googlecode.propidle.properties.Properties.properties;
import com.googlecode.propidle.properties.Properties;
import static com.googlecode.totallylazy.Sequences.sequence;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PropertiesTest {
    @Test
    public void shouldUnionTwoPropertiesFiles() {
        java.util.Properties result = Properties.compose(properties("a=1\nb=SHOULD_BE_OVERRIDEN"), properties("b=2\nc=3"));

        assertThat(result.getProperty("a"), is("1"));
        assertThat(result.getProperty("b"), is("2"));
        assertThat(result.getProperty("c"), is("3"));
    }

    @Test
    public void shouldBeAbleToUnionPropertiesWithFold() {
        java.util.Properties result = sequence(properties("a=SHOULD_BE_OVERRIDEN\nb=SHOULD_BE_OVERRIDEN"), properties("b=2\nc=3"), properties("a=1")).fold(new java.util.Properties(), Properties.compose());

        assertThat(result.getProperty("a"), is("1"));
        assertThat(result.getProperty("b"), is("2"));
        assertThat(result.getProperty("c"), is("3"));
    }
}
