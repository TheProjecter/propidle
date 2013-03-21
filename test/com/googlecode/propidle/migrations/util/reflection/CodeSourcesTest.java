package com.googlecode.propidle.migrations.util.reflection;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.googlecode.propidle.migrations.util.matchers.UrlMatching.urlMatching;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.Matchers.predicate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CodeSourcesTest {
    @Test
    public void listsAllFilesInJar() throws IOException {
        Iterable<CodeSourceEntry> resourcesInJunitJar = CodeSources.resourcesInCodeSource(Test.class);
        Sequence<URL> urls = sequence(resourcesInJunitJar).map(asUrl());

        Option<URL> runWithUrl = urls.filter(predicate(urlMatching(".*RunWith.class"))).headOption();

        assertThat(runWithUrl.isEmpty(), is(false));
        checkUrlCanBeStreamed(runWithUrl.get());
    }

    private Callable1<? super CodeSourceEntry, URL> asUrl() {
        return new Callable1<CodeSourceEntry, URL>() {
            public URL call(CodeSourceEntry codeSourceEntry) throws Exception {
                return codeSourceEntry.url();
            }
        };
    }

    private void checkUrlCanBeStreamed(URL url) throws IOException {
        InputStream stream = url.openStream();
        stream.close();
    }

}
