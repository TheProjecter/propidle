package com.googlecode.propidle.client.loaders;

import com.googlecode.propidle.urls.MimeType;
import com.googlecode.propidle.urls.UriGetter;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import static com.googlecode.propidle.client.loaders.PropertiesAtUrl.propertiesAtUrl;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class PropertiesAtUrlTest {

    private UriGetter uriGetter = mock(UriGetter.class);
    private URL first;
    private URL second;

    @Before
    public void setUp() throws MalformedURLException {
        first = new URL("http://first");
        second = new URL("http://second");
    }

    @Test
    public void shouldLoadFromFirstUrlIfItWorks() throws Exception {
        when(uriGetter.get(eq(first.toURI()), any(MimeType.class))).thenReturn(new ByteArrayInputStream("a=b".getBytes()));

        Properties actualProperties = propertiesAtUrl(uriGetter, first, second).call();

        verify(uriGetter, never()).get(eq(second.toURI()), any(MimeType.class));
        assertThat(actualProperties, is(equalTo(properties("a", "b"))));
    }

    @Test
    public void shouldLoadPropertiesFromSecondUrlIfFirstFails() throws Exception {
        when(uriGetter.get(eq(first.toURI()), any(MimeType.class))).thenThrow(new Exception());
        when(uriGetter.get(eq(second.toURI()), any(MimeType.class))).thenReturn(new ByteArrayInputStream("a=b".getBytes()));

        Properties actualProperties = propertiesAtUrl(uriGetter, first, second).call();

        assertThat(actualProperties, is(equalTo(properties("a", "b"))));
    }

    private Properties properties(final String key, final String value) {
        Properties secondProperties = new Properties();
        secondProperties.setProperty(key, value);
        return secondProperties;
    }

}
