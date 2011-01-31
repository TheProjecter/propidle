package com.googlecode.propidle.server.properties;

import com.googlecode.utterlyidle.BasePath;
import org.junit.Test;

import static com.googlecode.utterlyidle.BasePath.basePath;
import static com.googlecode.utterlyidle.io.Url.url;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UtterlyIdleUrlResolverTest {
    private BasePath basePath = basePath("/base");
    private final UtterlyIdleUrlResolver resolver = new UtterlyIdleUrlResolver(basePath);
    @Test
    public void shouldPrefixRelativeUrlsWithBasePath() {
        assertThat(resolver.resolve(url("/someresource")), is(url("/base/someresource")));

        assertThat(resolver.resolve(url("http://google.com")), is(url("http://google.com")));
    }
}
