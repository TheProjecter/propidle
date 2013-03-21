package com.googlecode.utterlyidle.migrations.util.reflection;

import com.googlecode.utterlyidle.migrations.Migrator;
import com.googlecode.utterlyidle.migrations.log.MigrationLog;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.migrations.util.matchers.UrlMatching.urlMatching;
import static com.googlecode.utterlyidle.migrations.util.reflection.CodeSourceEntry.getUrl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class PackagesTest {
    @Test
    public void listsFileInPackageOnFileSystem() throws Exception {
        Iterable<URL> files = sequence(Packages.filesInOrUnderPackage(Migrator.class)).map(getUrl());

        assertThat(files, Matchers.<URL>hasItem(whichLooksLikeAUrlFor(Migrator.class)));
        assertThat(files, Matchers.<URL>hasItem(whichLooksLikeAUrlFor(MigrationLog.class)));
        assertThat(files, not(Matchers.<URL>hasItem(whichLooksLikeAUrlFor(PackagesTest.class))));
    }

    @Test
    public void listsFileInPackageInAJar() throws Exception {
        Iterable<URL> files = sequence(Packages.filesInOrUnderPackage(RunWith.class)).map(getUrl());

        assertThat(files, Matchers.<URL>hasItem(urlMatching(".*RunWith.class")));
        assertThat(files, Matchers.<URL>hasItem(urlMatching(".*notification.Failure.class")));
    }

    private Matcher<URL> whichLooksLikeAUrlFor(Class aClass) {
        return urlMatching(".*" + aClass.getSimpleName() + ".class");
    }


}
