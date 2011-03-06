package com.googlecode.propidle.util.reflection;

import com.googlecode.propidle.util.Callables;
import static com.googlecode.propidle.util.matchers.UrlMatching.urlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.not;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

public class PackagesTest {
    @Test
    public void listsFileInPackageOnFileSystem() throws Exception {
        Iterable<URL> files = Packages.filesInSamePackageAs(PackagesTest.class);

        assertThat(files, Matchers.<URL>hasItem(whichLooksLikeAUrlFor(PackagesTest.class)));
        assertThat(files, not(Matchers.<URL>hasItem(whichLooksLikeAUrlFor(Callables.class))));
    }

    @Test
    public void listsFileInPackageInAJar() throws Exception {
        Iterable<URL> files = Packages.filesInSamePackageAs(RunWith.class);

        assertThat(files, Matchers.<URL>hasItem(urlMatching(".*RunWith.class")));
    }

    private Matcher<URL> whichLooksLikeAUrlFor(Class aClass) {
        return urlMatching(".*" + aClass.getSimpleName() + ".class");
    }
}
