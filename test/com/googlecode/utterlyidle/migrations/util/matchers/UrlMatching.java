package com.googlecode.utterlyidle.migrations.util.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.net.URL;

public class UrlMatching extends TypeSafeMatcher<URL> {
    private final String regex;

    public static Matcher<URL> urlMatching(final String regex) {
        return new UrlMatching(regex);
    }

    protected UrlMatching(String regex) {
        this.regex = regex;
    }

    public boolean matchesSafely(URL url) {
        return url.getPath().matches(regex);
    }

    public void describeTo(Description description) {
        description.appendText("url matching " + regex);
    }
}
