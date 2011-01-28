package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.PropertyComparison;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.Properties;

import static com.googlecode.propidle.Properties.properties;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.PropertyComparison.*;
import static com.googlecode.propidle.PropertyName.propertyName;
import static com.googlecode.propidle.PropertyValue.propertyValue;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class ChangeTest {
    private static final PropertiesPath SOME_PATH = propertiesPath("blah");
    private static final RevisionNumber SOME_REVISION = revisionNumber(1);

    @Test
    public void canBeAppliedToAPropertiesFile() throws Exception{
        Change newProperty = change(newProperty(propertyName("a"), propertyValue("some value")));
        assertThat(newProperty.applyTo(properties()), hasProperty("a", "some value"));

        Change changedProperty = change(changedProperty(propertyName("a"), propertyValue("old value doesn't have to match"), propertyValue("new value")));
        assertThat(changedProperty.applyTo(properties("a=old value")), hasProperty("a", "new value"));

        Change removedProperty = change(removedProperty(propertyName("a"), propertyValue("old value doesn't have to match")));
        assertThat(removedProperty.applyTo(properties("a=old value")), not(containsProperty("a")));
    }

    private Matcher<? super Properties> containsProperty(final String name) {
        return new TypeSafeMatcher<Properties>() {
            @Override
            protected boolean matchesSafely(Properties properties) {
                return properties.containsKey(name);
            }

            public void describeTo(Description description) {
                description.appendText(format("Properties file containing '%s'", name));
            }
        };
    }

    private Matcher<Properties> hasProperty(final String name, final String expectedValue) {
        return new TypeSafeMatcher<Properties>() {
            @Override
            protected boolean matchesSafely(Properties properties) {
                return expectedValue.equals(properties.get(name));
            }

            public void describeTo(Description description) {
                description.appendText(format("Properties file with %s=%s", name, expectedValue));
            }
        };
    }

    private Change change(PropertyComparison comparison) {
        return new Change(SOME_REVISION, SOME_PATH, comparison);
    }
}