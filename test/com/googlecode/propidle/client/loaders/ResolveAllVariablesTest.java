package com.googlecode.propidle.client.loaders;

import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.client.loaders.ResolveAllVariables.resolveAllProperties;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Properties.properties;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("unchecked")
public class ResolveAllVariablesTest {

    @Test
    public void shouldResolveMultiplePropertiesAutomatically() throws Exception{
        Properties properties = properties(pair("foo", "${bar}"), pair("fred", "${bar}"), pair("bar", "value"));
        Properties expectedProperties = properties(pair("foo", "value"), pair("fred", "value"), pair("bar", "value"));
        assertThat(resolveAllProperties(propertiesOf(properties)).call(), is(expectedProperties));
    }

    @Test
    public void shouldNotThrowExceptionWithUnresolvableProperty() throws Exception{
        Properties properties = properties(pair("foo", "${bar}"), pair("bar", "${woo}"));
        Properties expectedProperties = properties(pair("foo", "${woo}"), pair("bar", "${woo}"));
        assertThat(resolveAllProperties(propertiesOf(properties)).call(), is(expectedProperties));
    }

    @Test
    public void shouldReplacePartialTokenWithinPropertyValue() throws Exception{
        Properties properties = properties(pair("message", "What a nice ${foo} for a ${bar}"), pair("foo", "day"), pair("bar", "${fred}"), pair("fred", "walk"));
        Properties expectedProperties = properties(pair("message", "What a nice day for a walk"), pair("foo", "day"), pair("bar", "walk"), pair("fred", "walk"));
        assertThat(resolveAllProperties(propertiesOf(properties)).call(), is(expectedProperties));
    }

    @Test
    public void shouldReplaceTransientProperties() throws Exception{
        Properties properties = properties(pair("foo", "${bar}"), pair("fred", "${cheese}"), pair("bar", "${fred}"), pair("cheese", "bacon"));
        Properties expectedProperties = properties(pair("foo", "bacon"), pair("fred", "bacon"), pair("bar", "bacon"), pair("cheese", "bacon"));
        assertThat(resolveAllProperties(propertiesOf(properties)).call(), is(expectedProperties));
    }

    @Test
    public void shouldReplaceTransientPropertiesEndingWithNonexistentProperty() throws Exception{
        Properties properties = properties(pair("foo", "${bar}"), pair("bar", "${fred}"));
        Properties expectedProperties = properties(pair("foo", "${fred}"), pair("bar", "${fred}"));
        assertThat(resolveAllProperties(propertiesOf(properties)).call(), is(expectedProperties));
    }

    @Test
    public void shouldNotResolveLoopingProperties() throws Exception{
        Properties properties = properties(pair("foo", "${bob}"), pair("bar", "${foo}"), pair("bob", "${bar}"));
        Properties expectedProperties = properties(pair("foo", "${bob}"), pair("bar", "${foo}"), pair("bob", "${bar}"));
        assertThat(resolveAllProperties(propertiesOf(properties)).call(), is(expectedProperties));
    }

    @Test
    public void shouldExceptAllTypesOfCharacters() throws Exception{
        Properties properties = properties(pair("foo.url", "${boo_url}"), pair("boo_url", "${cheese-dash\\au/boo}"), pair("cheese-dash\\au/boo", "bob"));
        Properties expectedProperties = properties(pair("foo.url", "bob"), pair("boo_url", "bob"), pair("cheese-dash\\au/boo", "bob"));
        assertThat(resolveAllProperties(propertiesOf(properties)).call(), is(expectedProperties));
    }

    private static Callable<Properties> propertiesOf(final Properties properties) {
        return new Callable<Properties>() {
            public Properties call() throws Exception {
                return properties;
            }
        };
    }

}