package com.googlecode.propidle.util.matchers;

import com.googlecode.totallylazy.regex.Regex;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class RegexMatcher extends TypeSafeMatcher<String> {
    private final Regex regex;

    public static RegexMatcher matches(String regex){
        return new RegexMatcher(regex);
    }

    public RegexMatcher(String regex){
        this.regex = Regex.regex(regex);
    }


    @Override
    protected boolean matchesSafely(String value) {
        return !regex.findMatches(value).headOption().isEmpty();
    }

    public void describeTo(Description description) {
        description.appendText("matches ");
        description.appendText(regex.toString());
    }
}