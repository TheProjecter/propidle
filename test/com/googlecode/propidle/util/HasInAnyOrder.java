package com.googlecode.propidle.util;

import static com.googlecode.totallylazy.Sequences.sequence;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.List;

public class HasInAnyOrder<T> extends TypeSafeMatcher<Iterable<T>> {
    private final Iterable<T> expected;

    public static <T> Matcher<Iterable<T>> hasInAnyOrder(Iterable<T> expected) {
        return new HasInAnyOrder<T>(expected);
    }

    public HasInAnyOrder(Iterable<T> expected) {
        this.expected = expected;
    }

    @Override
    public boolean matchesSafely(Iterable<T> actual) {
        List<T> expected = sequence(this.expected).toList();
        List<T> actualList = sequence(actual).toList();
        if (expected.size() != actualList.size()) return false;
        for (T item : actual) {
            boolean wasPresent = expected.remove(item);
            if (!wasPresent) return false;
        }
        return true;
    }

    public void describeTo(Description description) {
        description.appendText("These values in any order: " + sequence(expected));
    }
}
