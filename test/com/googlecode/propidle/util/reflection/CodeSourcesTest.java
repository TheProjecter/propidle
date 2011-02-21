package com.googlecode.propidle.util.reflection;

import static com.googlecode.propidle.util.matchers.UrlMatching.urlMatching;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.Matchers.predicate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CodeSourcesTest {
    @Test
    public void listsAllFilesInJar() throws IOException {
        Iterable<URL> resourcesInJunitJar = CodeSources.resourcesInCodeSource(Test.class);
        Sequence<URL> urls = sequence(resourcesInJunitJar);

        Option<URL> runWithUrl = urls.filter(predicate(urlMatching(".*RunWith.class"))).headOption();

        assertThat(runWithUrl.isEmpty(), is(false));
        checkUrlCanBeStreamed(runWithUrl.get());
    }

    private void checkUrlCanBeStreamed(URL url) throws IOException {
        InputStream stream = url.openStream();
        stream.close();
    }

}
