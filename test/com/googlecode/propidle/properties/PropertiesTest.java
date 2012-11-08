package com.googlecode.propidle.properties;

import com.googlecode.propidle.client.properties.Properties;
import org.junit.Test;

import static com.googlecode.propidle.client.properties.Properties.properties;
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

    @Test
    public void shouldNotBlowIfThePropertyValueIsVeryLong(){
        java.util.Properties result = Properties.compose(properties(String.format("a=1\nb=%s",veryLongString())));
        assertThat(result.getProperty("b"),is(veryLongString()));
    }

    private String veryLongString() {
        String value="";
        for(int i=0;i<2000;i++)
            value+="x";
        return value;
    }
}
